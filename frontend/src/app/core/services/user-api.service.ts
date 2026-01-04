import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { HttpService } from './http.service';
import { Role } from '../enums/role.enum';

export interface User {
  id: string;
  name: string;
  email: string;
}

export interface CreateUserPayload {
  name: string;
  email: string;
  password: string;
  role: Role
}

@Injectable({
  providedIn: 'root',
})
export class UserApiService {
  constructor(private http: HttpService) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(API_ENDPOINTS.users.list);
  }

  getUserById(id: string): Observable<User> {
    return this.http.get<User>(API_ENDPOINTS.users.detail(id));
  }

  createUser(payload: CreateUserPayload): Observable<void> {
    return this.http.post<void>(API_ENDPOINTS.users.list, payload);
  }
}
