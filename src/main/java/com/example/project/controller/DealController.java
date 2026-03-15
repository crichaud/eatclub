package com.example.project.controller;

import com.example.project.dto.RestaurantDealDTO;
import com.example.project.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DealController {
    @Autowired
    private DealService dealService;

    @GetMapping("/deals")
    public Map<String, List<RestaurantDealDTO>> getDeals(@RequestParam String timeOfDay) {
        return Collections.singletonMap("deals", dealService.getActiveDeals(timeOfDay));
    }
}
