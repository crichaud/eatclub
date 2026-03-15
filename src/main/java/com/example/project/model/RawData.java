package com.example.project.model;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class RawData {

    private List<RawDeal> deals;
    private List<Restaurant> restaurants;


    public List<RawDeal> getDeals() {
        return deals;
    }

    public void setDeals(List<RawDeal> deals) {
        this.deals = deals;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
