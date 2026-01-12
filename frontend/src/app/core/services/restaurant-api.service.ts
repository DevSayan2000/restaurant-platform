import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { HttpService } from './http.service';
import { Role } from '../enums/role.enum';
import { body } from '@primeuix/themes/aura/card';
import { AuthService, User } from '../auth/auth.service';

export interface Restaurant {
  id: number;
  name: string;
  city: string;
  foodType: 'VEG' | 'NON_VEG' | 'JAIN' | 'VEGAN'; // ← broaden enum if needed
  cuisine: string;
  avgRating: number;
  createdBy: string;
  createdDate: string; // ISO string
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
}
