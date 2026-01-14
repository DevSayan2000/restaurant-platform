package com.example.restaurantplatform.dto.analytics;

import com.example.restaurantplatform.mapper.PopularRestaurantMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PopularRestaurants {

    List<PopularRestaurantMapper> restaurants;
}