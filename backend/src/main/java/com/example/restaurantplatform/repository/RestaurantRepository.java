package com.example.restaurantplatform.repository;

import com.example.restaurantplatform.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByCity(String city);
}