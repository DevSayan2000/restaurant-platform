package com.example.restaurantplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "footfall")
@Getter
@Setter
public class Footfall
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(name = "footfall_count")
    private Integer footfallCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}