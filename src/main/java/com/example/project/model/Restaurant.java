package com.example.project.model;
import lombok.Data;
import java.util.List;

@Data
public class Restaurant {
    private String objectId;
    private String name;
    private String address1;
    private String suburb;
    private String open;
    private String close;
    private List<RawDeal> deals;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
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

    public List<RawDeal> getDeals() {
        return deals;
    }

    public void setDeals(List<RawDeal> deals) {
        this.deals = deals;
    }
}
