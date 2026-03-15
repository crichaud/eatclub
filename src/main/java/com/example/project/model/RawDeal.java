package com.example.project.model;

import lombok.Data;

@Data
public class RawDeal {
    private String objectId;
    private String discount;
    private String dineIn;
    private String lightning;
    private String qtyLeft;

    // Time overrides found in some deals
    private String start;
    private String end;

    // Also include these if the JSON uses them at the deal level
    private String open;
    private String close;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}

