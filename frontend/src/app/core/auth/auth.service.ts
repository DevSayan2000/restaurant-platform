import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Role } from '../enums/role.enum';

export interface User {
  id: number;
  name: string;
  email: string;
  role: Role;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable();


  login(user: User) {
    this.userSubject.next(user);
  }

  logout() {
    this.userSubject.next(null);
    localStorage.clear();
    location.reload();
  }

  get isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }

  get user(): User | null {
    return this.userSubject.value;
  }
}
