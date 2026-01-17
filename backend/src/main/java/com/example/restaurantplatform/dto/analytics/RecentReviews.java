package com.example.restaurantplatform.dto.analytics;

import com.example.restaurantplatform.mapper.RecentReviewMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RecentReviews {

    List<RecentReviewMapper> reviews;
}