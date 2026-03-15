package com.example.project.mapper;

import com.example.project.dto.RestaurantDealDTO;
import com.example.project.model.RawDeal;
import com.example.project.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DealMapper {

    @Mapping(source = "restaurant.objectId", target = "restaurantObjectId")
    @Mapping(source = "restaurant.name", target = "restaurantName")
    @Mapping(source = "restaurant.address1", target = "restaurantAddress1")
    @Mapping(source = "restaurant.suburb", target = "restaurantSuburb")
    @Mapping(source = "restaurant.open", target = "restaurantOpen")
    @Mapping(source = "restaurant.close", target = "restaurantClose")

    @Mapping(source = "deal.objectId", target = "dealObjectId")
    @Mapping(source = "deal.discount", target = "discount")
    @Mapping(source = "deal.dineIn", target = "dineIn")
    @Mapping(source = "deal.lightning", target = "lightning")
    @Mapping(source = "deal.qtyLeft", target = "qtyLeft")
    RestaurantDealDTO toDto(Restaurant restaurant, RawDeal deal);
}
