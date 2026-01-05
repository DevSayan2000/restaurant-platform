import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { HttpService } from './http.service';
import { Role } from '../enums/role.enum';
import { body } from '@primeuix/themes/aura/card';
import { AuthService, User } from '../auth/auth.service';

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

@Injectable({
  providedIn: 'root',
})
export class UserApiService {
  constructor(private http: HttpService, private authService: AuthService) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(API_ENDPOINTS.users.list);
  }

  getUserById(id: string): Observable<User> {
    return this.http.get<User>(API_ENDPOINTS.users.detail(id));
  }

  createUser(payload: CreateUserPayload): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(API_ENDPOINTS.users.list, payload);
  }

  userLogin(payload: UserLoginPayload): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(API_ENDPOINTS.auth.login, payload);
  }

  getLoggedInUser(): Observable<User> {
    return this.http.get<User>(API_ENDPOINTS.auth.profile).pipe(
      tap((user) => {
        this.authService.login(user);
      })
    );
  }
}
