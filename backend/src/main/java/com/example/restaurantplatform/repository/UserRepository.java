package com.example.restaurantplatform.repository;

import com.example.restaurantplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}