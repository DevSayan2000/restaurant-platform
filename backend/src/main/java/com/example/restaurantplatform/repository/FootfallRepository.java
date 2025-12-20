package com.example.restaurantplatform.repository;

import com.example.restaurantplatform.entity.Footfall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FootfallRepository extends JpaRepository<Footfall, Long> {

    @Query("SELECT AVG(f.footfallCount) FROM Footfall f WHERE f.restaurant.id = :restaurantId")
    Double findAverageFootfall(Long restaurantId);
}