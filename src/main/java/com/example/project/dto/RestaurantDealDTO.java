package com.example.project.dto;

import lombok.Data;

@Data
public class RestaurantDealDTO {
    private String restaurantObjectId;
    private String restaurantName;
    private String restaurantAddress1;
    private String restaurantSuburb; // Note: typo matches your requirement
    private String restaurantOpen;
    private String restaurantClose;
    private String dealObjectId;
    private String discount;
    private String dineIn;
    private String lightning;
    private String qtyLeft;


    public String getRestaurantObjectId() {
        return restaurantObjectId;
    }

    public void setRestaurantObjectId(String restaurantObjectId) {
        this.restaurantObjectId = restaurantObjectId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress1() {
        return restaurantAddress1;
    }

    public void setRestaurantAddress1(String restaurantAddress1) {
        this.restaurantAddress1 = restaurantAddress1;
    }

    public String getRestaurantSuburb() {
        return restaurantSuburb;
    }

    public void setRestaurantSuburb(String restaurantSuburb) {
        this.restaurantSuburb = restaurantSuburb;
    }

    public String getRestaurantOpen() {
        return restaurantOpen;
    }

    public void setRestaurantOpen(String restaurantOpen) {
        this.restaurantOpen = restaurantOpen;
    }

    public String getRestaurantClose() {
        return restaurantClose;
    }

    public void setRestaurantClose(String restaurantClose) {
        this.restaurantClose = restaurantClose;
    }

    public String getDealObjectId() {
        return dealObjectId;
    }

    public void setDealObjectId(String dealObjectId) {
        this.dealObjectId = dealObjectId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDineIn() {
        return dineIn;
    }

    public void setDineIn(String dineIn) {
        this.dineIn = dineIn;
    }

    public String getLightning() {
        return lightning;
    }

    public void setLightning(String lightning) {
        this.lightning = lightning;
    }

    public String getQtyLeft() {
        return qtyLeft;
    }

    public void setQtyLeft(String qtyLeft) {
        this.qtyLeft = qtyLeft;
    }
}
