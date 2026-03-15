package com.example.project.service;

import com.example.project.dto.PeakWindowDTO;
import com.example.project.dto.RestaurantDealDTO;
import com.example.project.mapper.DealMapper;
import com.example.project.model.RawData;

import com.example.project.model.RawDeal;
import com.example.project.model.Restaurant;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@Service
public class DealService {

    private final RestTemplate restTemplate;
    private final DealMapper dealMapper;
    private final String DATA_URL = "https://eccdn.com.au/misc/challengedata.json";

    @Autowired
    public DealService(DealMapper dealMapper, RestTemplate restTemplate) {
        this.dealMapper = dealMapper;
        this.restTemplate = restTemplate;
    }

    public List<RestaurantDealDTO> getActiveDeals(String timeStr) {
        RawData response = restTemplate.getForObject(DATA_URL, RawData.class);
        LocalTime queryTime = parseTime(timeStr);

        if (response == null || response.getRestaurants() == null) {
            return Collections.emptyList();
        }

        return response.getRestaurants().stream()
                .flatMap(restaurant -> restaurant.getDeals().stream()
                        .filter(deal -> {
                            String open = (deal.getStart() != null) ? deal.getStart() : restaurant.getOpen();
                            String close = (deal.getEnd() != null) ? deal.getEnd() : restaurant.getClose();
                            return isWithinHours(queryTime, open, close);
                        })
                        .map(deal -> dealMapper.toDto(restaurant, deal))
                )
                .collect(Collectors.toList());
    }

    public PeakWindowDTO getPeakWindow() {
        RawData response = restTemplate.getForObject(DATA_URL, RawData.class);
        if (response == null || response.getRestaurants() == null) return null;

        List<TimeEvent> events = new ArrayList<>();

        for (Restaurant r : response.getRestaurants()) {
            for (RawDeal d : r.getDeals()) {
                // Determine effective window (Deal overrides vs Restaurant hours)
                LocalTime open = parseTime(firstNonNull(d.getStart(), d.getOpen(), r.getOpen()));
                LocalTime close = parseTime(firstNonNull(d.getEnd(), d.getClose(), r.getClose()));

                // Add +1 for every start, -1 for every end
                events.add(new TimeEvent(open, 1));
                events.add(new TimeEvent(close, -1));
            }
        }

        // Sort by time. If times are equal, process starts (+1) before ends (-1)
        events.sort(Comparator.comparing(TimeEvent::getTime)
                .thenComparing(e -> -e.getType()));

        int maxDeals = 0;
        int currentDeals = 0;
        LocalTime peakStart = null;
        LocalTime peakEnd = null;

        for (int i = 0; i < events.size(); i++) {
            TimeEvent event = events.get(i);
            currentDeals += event.getType();

            if (currentDeals > maxDeals) {
                maxDeals = currentDeals;
                peakStart = event.getTime();
                // The window lasts until the next event changes the count
                if (i + 1 < events.size()) {
                    peakEnd = events.get(i + 1).getTime();
                }
            }
        }

        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("h:mma");
        PeakWindowDTO dto = new PeakWindowDTO();
        dto.setPeakTimeStart(peakStart.format(displayFormatter).toLowerCase());
        dto.setPeakTimeEnd(peakEnd.format(displayFormatter).toLowerCase());

        return dto;
    }

    @Data
    private static class TimeEvent {
        private final LocalTime time;
        private final int type; // 1 for start, -1 for end

        public TimeEvent(LocalTime time, int type) {
            this.time = time;
            this.type = type;
        }

        // Manually adding these resolves the "cannot resolve" error
        public LocalTime getTime() {
            return time;
        }

        public int getType() {
            return type;
        }
    }

    private boolean isWithinHours(LocalTime target, String openStr, String closeStr) {
        LocalTime open = parseTime(openStr);
        LocalTime close = parseTime(closeStr);

        // Standard Window (e.g., 9:00am to 3:00pm)
        if (open.isBefore(close)) {
            // Query must be >= open AND strictly < close
            return !target.isBefore(open) && target.isBefore(close);
        }

        // Overnight Window (e.g., 6:00pm to 2:00am)
        // Query must be >= open OR strictly < close
        return !target.isBefore(open) || target.isBefore(close);
    }

    private LocalTime parseTime(String time) {
        String cleaned = time.trim().toLowerCase();

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                // Pattern 1: 12-hour with AM/PM (e.g., 3:00pm or 3:00 pm)
                .appendOptional(DateTimeFormatter.ofPattern("h:mm[ ]a", Locale.ENGLISH))
                // Pattern 2: 24-hour (e.g., 15:00 or 10:30)
                .appendOptional(DateTimeFormatter.ofPattern("H:mm"))
                // Pattern 3: Simple hour with AM/PM (e.g., 3pm)
                .appendOptional(DateTimeFormatter.ofPattern("h[ ]a", Locale.ENGLISH))
                .toFormatter(Locale.ENGLISH);

        return LocalTime.parse(cleaned, formatter);
    }
}