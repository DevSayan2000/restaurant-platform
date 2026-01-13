package com.example.restaurantplatform.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AllReviewsResponse {
    private Integer total;
    private List<ReviewResponse> reviews;
}