package com.example.restaurantplatform.dto.analytics;

import com.example.restaurantplatform.mapper.PopularReviewMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PopularReviews {

    List<PopularReviewMapper> reviews;
}