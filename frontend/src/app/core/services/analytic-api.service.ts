import { Injectable } from '@angular/core';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { HttpService } from './http.service';
import { Restaurant, RestaurantReview } from './restaurant-api.service';

export interface Analytics {
  reviews: number;
  restaurants: number;
  users: number;
  rating: number;
}

@Injectable({
  providedIn: 'root',
})
export class AnalyticsApiService {
  constructor(private http: HttpService) {}

  getAnalytics() {
    return this.http.get<Analytics>(API_ENDPOINTS.analytics.analytics);
  }

  getRecentReviews() {
    return this.http.get<{ reviews: RestaurantReview[] }>(API_ENDPOINTS.analytics.recentReviews);
  }

  getPopularRestaurants() {
    return this.http.get<{ restaurants: Restaurant[] }>(API_ENDPOINTS.analytics.popularRestaurants);
  }
}
