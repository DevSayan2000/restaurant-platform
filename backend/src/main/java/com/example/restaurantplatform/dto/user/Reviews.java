package com.example.restaurantplatform.dto.user;

import com.example.restaurantplatform.mapper.ReviewMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Reviews {

    List<ReviewMapper> reviews;
}