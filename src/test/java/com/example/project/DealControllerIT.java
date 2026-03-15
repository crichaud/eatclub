package com.example.project;

import com.example.project.model.RawData;
import com.example.project.model.RawDeal;
import com.example.project.model.Restaurant;
import com.example.project.service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        classes = MyRestaurantApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class DealControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestTemplate restTemplate;

    private RawData fullMockData;

    @Autowired
    private DealService dealService;

    @BeforeEach
    void setUp() {
        // Setup Masala Kitchen (3pm - 9pm)
        Restaurant masala = createRestaurant("Masala Kitchen", "3:00pm", "9:00pm");
        masala.setDeals(Arrays.asList(createDeal("D1"), createDeal("D2")));

        // Setup ABC Chicken (12pm - 11pm)
        Restaurant abc = createRestaurant("ABC Chicken", "12:00pm", "11:00pm");
        abc.setDeals(Arrays.asList(createDeal("D3"), createDeal("D4")));

        // Setup Kekou (1pm - 11pm) with specific deal overrides
        Restaurant kekou = createRestaurant("Kekou", "1:00pm", "11:00pm");
        RawDeal k1 = createDeal("D5"); k1.setStart("2:00pm"); k1.setEnd("9:00pm");
        RawDeal k2 = createDeal("D6"); k2.setStart("5:00pm"); k2.setEnd("9:00pm");
        kekou.setDeals(Arrays.asList(k1, k2));

        // Setup OzzyThai (8am - 3pm)
        Restaurant ozzy = createRestaurant("OzzyThai", "8:00am", "3:00pm");
        ozzy.setDeals(Arrays.asList(createDeal("D7"), createDeal("D8")));

        // Setup Vrindavan (6pm - 9pm)
        Restaurant vrindavan = createRestaurant("Vrindavan", "6:00pm", "9:00pm");
        vrindavan.setDeals(Collections.singletonList(createDeal("D9")));

        // Setup Gyoza Gyoza (4pm - 10pm)
        Restaurant gyoza = createRestaurant("Gyoza Gyoza", "4:00pm", "10:00pm");
        gyoza.setDeals(Arrays.asList(createDeal("D10"), createDeal("D11")));

        fullMockData = new RawData();
        fullMockData.setRestaurants(Arrays.asList(masala, abc, kekou, ozzy, vrindavan, gyoza));

        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(RawData.class)))
                .thenReturn(fullMockData);
    }

    @Test
    void shouldReturn5DealsFor3pm() throws Exception {
        // Expected: Masala (2), ABC (2), Kekou Deal 1 (1). OzzyThai is excluded (closes at 3).
        mockMvc.perform(get("/api/v1/deals").param("timeOfDay", "3:00pm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deals", hasSize(5)))
                .andExpect(jsonPath("$.deals[*].restaurantName", hasItem("Masala Kitchen")))
                .andExpect(jsonPath("$.deals[*].restaurantName", not(hasItem("OzzyThai"))));
    }

    @Test
    void shouldReturn9DealsFor6pm() throws Exception {
        // Expected: All dinner venues active. OzzyThai excluded.
        mockMvc.perform(get("/api/v1/deals").param("timeOfDay", "6:00pm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deals", hasSize(9)));
    }

    @Test
    void shouldReturn4DealsFor9pm() throws Exception {
        // Expected: Only ABC Chicken (2) and Gyoza Gyoza (2) are open PAST 9:00pm.
        // Masala, Vrindavan, and Kekou deals end EXACTLY at 9:00pm and are excluded.
        mockMvc.perform(get("/api/v1/deals").param("timeOfDay", "9:00pm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deals", hasSize(4)))
                .andExpect(jsonPath("$.deals[*].restaurantName", containsInAnyOrder("ABC Chicken", "ABC Chicken", "Gyoza Gyoza", "Gyoza Gyoza")));
    }

    @Test
    void shouldReturnPeakWindow_Integration() throws Exception {
        // Act & Assert
        // The real DealService will now process 'fullMockData'
        // and calculate the 5pm-9pm window itself.
        mockMvc.perform(get("/api/v1/peak-window")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peakTimeStart", is("6:00pm")))
                .andExpect(jsonPath("$.peakTimeEnd", is("9:00pm")));
    }

    // Helper Methods
    private Restaurant createRestaurant(String name, String open, String close) {
        Restaurant r = new Restaurant();
        r.setName(name);
        r.setOpen(open);
        r.setClose(close);
        return r;
    }

    private RawDeal createDeal(String id) {
        RawDeal d = new RawDeal();
        d.setObjectId(id);
        return d;
    }

    @Test
    void shouldHandleInvalidTimeFormat() throws Exception {
        mockMvc.perform(get("/api/v1/deals")
                        .param("timeOfDay", "invalid-time"))
                .andExpect(status().isBadRequest());
    }
}
