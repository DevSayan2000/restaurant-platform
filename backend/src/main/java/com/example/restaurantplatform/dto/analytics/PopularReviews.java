package com.example.restaurantplatform.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PopularReviews {

    List<String> reviews;
}