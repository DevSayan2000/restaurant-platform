package com.example.restaurantplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    private String area;
    private String cuisine;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}