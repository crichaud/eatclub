package com.example.project;

import com.example.project.dto.PeakWindowDTO;
import com.example.project.dto.RestaurantDealDTO;
import com.example.project.mapper.DealMapper;
import com.example.project.model.RawData;
import com.example.project.model.RawDeal;
import com.example.project.model.Restaurant;
import com.example.project.service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private final DealMapper dealMapper = Mappers.getMapper(DealMapper.class);
    private DealService dealService;

    @BeforeEach
    void setUp() {
        dealService = new DealService(dealMapper, restTemplate);

        RawData generalData = new RawData();

        Restaurant ozzyThai = createRestaurant("OzzyThai", "8:00am", "3:00pm", null, null);
        Restaurant abcChicken = createRestaurant("ABC Chicken", "12:00pm", "11:00pm", null, null);
        Restaurant kekou = createRestaurant("Kekou", "1:00pm", "11:00pm", "5:00pm", "9:00pm");

        generalData.setRestaurants(Arrays.asList(ozzyThai, abcChicken, kekou));

        lenient().when(restTemplate.getForObject(anyString(), eq(RawData.class)))
                .thenReturn(generalData);
    }

    @Test
    void testTime_3pm_ExcludesClosingRestaurant() {
        List<RestaurantDealDTO> results = dealService.getActiveDeals("3:00pm");
        assertEquals(1, results.size());
        assertEquals("ABC Chicken", results.get(0).getRestaurantName());
    }

    @Test
    void testTime_6pm_IncludesAllActive() {
        List<RestaurantDealDTO> results = dealService.getActiveDeals("6:00pm");
        // ABC Chicken is open, Kekou's deal (5-9) is active
        assertEquals(2, results.size());
    }

    @Test
    void testTime_9pm_ExcludesEndingDeals() {
        List<RestaurantDealDTO> results = dealService.getActiveDeals("9:00pm");
        assertEquals(1, results.size());
        assertEquals("ABC Chicken", results.get(0).getRestaurantName());
    }

    @Test
    void shouldCalculatePeakWindow_5pmTo9pm() {
         List<Restaurant> peakRestaurants = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            peakRestaurants.add(createRestaurant("Rest" + i, "10:00am", "10:00pm", null, null));
        }

        peakRestaurants.add(createRestaurant("PeakRest", "12:00pm", "11:00pm", "5:00pm", "9:00pm"));
        peakRestaurants.get(7).setDeals(Arrays.asList(new RawDeal(), new RawDeal()));
        peakRestaurants.get(7).getDeals().get(0).setStart("5:00pm");
        peakRestaurants.get(7).getDeals().get(0).setEnd("9:00pm");
        peakRestaurants.get(7).getDeals().get(1).setStart("5:00pm");
        peakRestaurants.get(7).getDeals().get(1).setEnd("9:00pm");

        RawData peakData = new RawData();
        peakData.setRestaurants(peakRestaurants);


        when(restTemplate.getForObject(anyString(), eq(RawData.class))).thenReturn(peakData);


        PeakWindowDTO result = dealService.getPeakWindow();


        assertNotNull(result);
        assertEquals("5:00pm", result.getPeakTimeStart());
        assertEquals("9:00pm", result.getPeakTimeEnd());
    }


    private Restaurant createRestaurant(String name, String op, String cl, String dSt, String dEnd) {
        Restaurant r = new Restaurant();
        r.setName(name);
        r.setOpen(op);
        r.setClose(cl);
        RawDeal d = new RawDeal();
        d.setStart(dSt);
        d.setEnd(dEnd);
        r.setDeals(new ArrayList<>(Collections.singletonList(d)));
        return r;
    }
}
