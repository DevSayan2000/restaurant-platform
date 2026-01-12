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

export interface RestaurantPayload {
  name: string;
  city: string;
  foodType: FoodType;
  cuisine: string;
}

@Injectable({
  providedIn: 'root',
})
export class RestaurantApiService {
  constructor(private http: HttpService) {}

  getRestaurants(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(API_ENDPOINTS.restaurants.list);
  }

  getUserRestaurants(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(API_ENDPOINTS.restaurants.userList);
  }

  createRestaurant(body: RestaurantPayload): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(API_ENDPOINTS.restaurants.list, body);
  }

  deleteRestaurant(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(API_ENDPOINTS.restaurants.delete(id));
  }
}
