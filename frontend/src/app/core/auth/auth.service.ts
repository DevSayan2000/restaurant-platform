import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
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

  constructor(private router: Router) {}

  login(user: User) {
    this.userSubject.next(user);
  }

  logout() {
    this.userSubject.next(null);
    localStorage.clear();
    this.router.navigate(['/sign-in']);
  }

  get isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }

  get user(): User | null {
    return this.userSubject.value;
  }
}
