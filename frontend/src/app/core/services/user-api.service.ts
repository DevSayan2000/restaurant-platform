import { Injectable } from '@angular/core';
import { Observable, retry, tap, timer } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { HttpService } from './http.service';
import { Role } from '../enums/role.enum';
import { AuthService, User } from '../auth/auth.service';
import { Restaurant, RestaurantReview } from './restaurant-api.service';

export interface CreateUserPayload {
  name: string;
  email: string;
  password: string;
  role: Role;
}

export interface UserLoginPayload {
  email: string;
  password: string;
}

export interface UserReview {
  id: number;
  rating: number;
  review: string;
  restaurant: string;
  createdDate: string;
}

export interface UpdateUserNamePayload {
  name: string;
}

export interface UpdateUserPasswordPayload {
  currentPassword: string;
  newPassword: string;
}

@Injectable({
  providedIn: 'root',
})
export class UserApiService {
  constructor(private http: HttpService, private authService: AuthService) {}

  getUsers() {
    return this.http.get<{ userResponses: User[] }>(API_ENDPOINTS.users.list);
  }

  getUserById(id: string): Observable<User> {
    return this.http.get<User>(API_ENDPOINTS.users.detail(id));
  }

  createUser(payload: CreateUserPayload): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(API_ENDPOINTS.users.list, payload);
  }

  updateUser(payload: UpdateUserNamePayload | UpdateUserPasswordPayload): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(API_ENDPOINTS.users.list, payload);
  }

  userLogin(payload: UserLoginPayload): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(API_ENDPOINTS.auth.login, payload);
  }

  getLoggedInUser(): Observable<User> {
    return this.http.get<User>(API_ENDPOINTS.auth.profile).pipe(
      retry({ count: 1, delay: () => timer(1000) }),
      tap((user) => {
        this.authService.login(user);
      })
    );
  }

  deleteUser(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(API_ENDPOINTS.users.delete(id));
  }

  reviewedRestaurants() {
    return this.http.get<{ restaurantResponses: Restaurant[] }>(API_ENDPOINTS.users.reviewedRestaurants);
  }

  userReviews() {
    return this.http.get<{ reviews: UserReview[] }>(API_ENDPOINTS.users.userReviews);
  }
}
