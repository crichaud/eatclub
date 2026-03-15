package com.example.project.service;

import com.example.project.dto.RestaurantDealDTO;
import com.example.project.mapper.DealMapper;
import com.example.project.model.RawData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import java.util.*;
import java.util.stream.Collectors;

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