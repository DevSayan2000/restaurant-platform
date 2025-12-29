package com.example.restaurantplatform.repository;

import com.example.restaurantplatform.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByCity(String city);
    List<Restaurant> findByEmail(String email);

    @Query("SELECT r FROM Restaurant r WHERE r.email = :email AND r.city = :city")
    List<Restaurant> findByEmailAndCity(String email, String city);
}