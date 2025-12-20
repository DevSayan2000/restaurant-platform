package com.example.restaurantplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}