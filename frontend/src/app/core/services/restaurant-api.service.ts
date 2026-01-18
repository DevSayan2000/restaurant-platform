import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { HttpService } from './http.service';
import { Role } from '../enums/role.enum';
import { body } from '@primeuix/themes/aura/card';
import { AuthService, User } from '../auth/auth.service';
import { FoodType } from '../enums/food-type.enum';

export interface Restaurant {
  id: number;
  name: string;
  city: string;
  foodType: FoodType;
  cuisine: string;
  avgRating: number;
  createdBy: string;
  createdDate: string; // ISO string
}

export interface RestaurantReview {
  id: number;
  rating: number;
  review: string;
  createdDate: string;
  createdBy: string;
  userId: number;
  restaurant?: string;
}

export interface RestaurantPayload {
  name: string;
  city: string;
  foodType: FoodType;
  cuisine: string;
}

export interface RestaurantReviewPayload {
  rating: number;
  review: string;
}

@Injectable({
  providedIn: 'root',
})
export class RestaurantApiService {
  constructor(private http: HttpService) {}

  getRestaurants() {
    return this.http.get<{ restaurantResponses: Restaurant[] }>(API_ENDPOINTS.restaurants.list);
  }

  getUserRestaurants() {
    return this.http.get<{ restaurantResponses: Restaurant[] }>(API_ENDPOINTS.restaurants.userList);
  }

  createRestaurant(body: RestaurantPayload): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(API_ENDPOINTS.restaurants.list, body);
  }

  deleteRestaurant(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(API_ENDPOINTS.restaurants.delete(id));
  }

  getRestaurantDetails(id: string): Observable<Restaurant> {
    return this.http.get<Restaurant>(API_ENDPOINTS.restaurants.details(id));
  }

  getRestaurantReviews(id: string) {
    return this.http.get<{ reviews: RestaurantReview[], maxResults: number }>(API_ENDPOINTS.restaurants.reviews(id));
  }

  addRestaurantReview(id: string, body: RestaurantReviewPayload): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(API_ENDPOINTS.restaurants.addReview(id), body);
  }

  deleteRestaurantReview(id: string): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(API_ENDPOINTS.restaurants.deleteReview(id));
  }
}
