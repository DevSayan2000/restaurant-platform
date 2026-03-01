# Restaurant Platform

A full-stack web application that lists restaurants in a city along with **user-submitted ratings** and **footfall data**, built to demonstrate **clean backend APIs, relational data modeling, and frontend integration**.

**Project Type:** Personal / Portfolio Project  
**Cost:** $0 (runs locally)  
**Tech Stack:** Java 21, Spring Boot, Angular, PostgreSQL  

### 🌐 Live Demo

| Service | URL |
|---------|-----|
| Frontend | [https://restaurant-platform-chi.vercel.app](https://restaurant-platform-chi.vercel.app) |
| Backend API | [https://restaurant-backend-qw6k.onrender.com/api](https://restaurant-backend-qw6k.onrender.com/api) |
| Health Check | [https://restaurant-backend-qw6k.onrender.com/api/actuator/health](https://restaurant-backend-qw6k.onrender.com/api/actuator/health) |

> **Note:** Backend is hosted on Render free tier — first request may take 30-60 seconds (cold start).

---

## Architecture Overview (Simplified)

┌──────────────────────────────────────────────┐
│ Web Client (Angular) │
└──────────────────────────────────────────────┘
│
▼
┌──────────────────────────────────────────────┐
│ Backend API (Spring Boot) │
│ REST APIs │ Validation │ Business Logic │
└──────────────────────────────────────────────┘
│
▼
┌──────────────────────────────────────────────┐
│ PostgreSQL Database │
│ Restaurants │ Ratings │ Footfall Data │
└──────────────────────────────────────────────┘


> Single-instance architecture — no microservices or message queues required for this project scope.

---

## Key Features

### Core Functionality
- Browse restaurants by city
- View average ratings per restaurant
- Track daily and historical footfall data
- Submit user ratings (1–5 scale)
- RESTful APIs with proper validation and error handling

### Technical Highlights
- Layered architecture (Controller → Service → Repository)
- DTO-based request/response models
- Pagination and sorting for restaurant listings
- Swagger / OpenAPI documentation for all endpoints

---

## Tech Stack

### Backend
- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Maven**
- **Swagger / OpenAPI**

### Frontend
- **Angular**
- **TypeScript**
- **REST API integration**

### Database
- **PostgreSQL**
- Normalized relational schema with constraints and indexing

---

## Project Structure

RestaurantPlatform/
├── README.md
├── backend/
│ ├── src/main/java
│ │ ├── controller
│ │ ├── service
│ │ ├── repository
│ │ └── model
│ ├── src/main/resources
│ │ └── application.yml
│ └── pom.xml
│
├── frontend/
│ ├── src/
│ └── angular.json
│
└── database/
└── schema.sql


---

## API Overview

### Restaurants
GET /api/restaurants - List restaurants
GET /api/restaurants/{id} - Get restaurant details
POST /api/restaurants - Add a restaurant


### Ratings & Footfall
POST /api/ratings - Submit rating
GET /api/ratings/{restaurantId} - Get ratings
GET /api/footfall/{restaurantId} - Get footfall data


### API Documentation
- Swagger UI available at:  
  `http://localhost:8080/swagger-ui.html`

---

## Getting Started

### Prerequisites
- Java 21
- Node.js + npm
- PostgreSQL

### Run Backend
```bash
cd backend
mvn spring-boot:run
Run Frontend
cd frontend
npm install
ng serve
Learning Objectives
This project demonstrates:

Backend development using Spring Boot

REST API design and validation

Relational database modeling with PostgreSQL

Angular and backend integration

Clean code practices and layered architecture

API documentation using OpenAPI

License
Copyright (c) 2026 Sayandeep Ghosh

All rights reserved.

This source code is proprietary and confidential.
Unauthorized copying, modification, distribution, or use of this software,
via any medium, is strictly prohibited.

Author
Personal Project – December 2025

Last Updated: February 5, 2026



