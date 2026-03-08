package com.example.restaurantplatform.repository;

import com.example.restaurantplatform.entity.MenuItem;
import com.example.restaurantplatform.enums.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantIdOrderByCategoryAscNameAsc(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndCategoryOrderByNameAsc(Long restaurantId, MenuCategory category);

    List<MenuItem> findByRestaurantIdAndAvailableTrueOrderByCategoryAscNameAsc(Long restaurantId);

    Optional<MenuItem> findByRestaurantIdAndNameIgnoreCase(Long restaurantId, String name);
}

