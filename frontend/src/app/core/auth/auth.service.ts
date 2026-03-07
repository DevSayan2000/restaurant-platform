import { Injectable, NgZone } from '@angular/core';
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
  private tokenExpiryTimer: any = null;

  constructor(private router: Router, private ngZone: NgZone) {
    // On app startup, if a token exists, schedule the expiry check
    this.scheduleTokenExpiryCheck();
  }

  login(user: User) {
    this.userSubject.next(user);
    this.scheduleTokenExpiryCheck();
  }

  logout(message?: string) {
    this.clearExpiryTimer();
    this.userSubject.next(null);
    localStorage.clear();
    if (message) {
      // Store the message so sign-in page can show it
      sessionStorage.setItem('logoutMessage', message);
    }
    this.router.navigate(['/sign-in']);
  }

  get isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    if (!token) return false;

    // Also check if token is expired
    if (this.isTokenExpired(token)) {
      this.logout('Your session has expired. Please sign in again.');
      return false;
    }
    return true;
  }

  get user(): User | null {
    return this.userSubject.value;
  }

  /**
   * Decode JWT payload (base64) to get expiry time
   */
  private getTokenExpiry(token: string): number | null {
    try {
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload));
      return decoded.exp ? decoded.exp * 1000 : null; // convert to milliseconds
    } catch {
      return null;
    }
  }

  /**
   * Check if the token is expired
   */
  private isTokenExpired(token: string): boolean {
    const expiry = this.getTokenExpiry(token);
    if (!expiry) return true;
    return Date.now() >= expiry;
  }

  /**
   * Schedule automatic logout when token expires
   */
  private scheduleTokenExpiryCheck() {
    this.clearExpiryTimer();

    const token = localStorage.getItem('token');
    if (!token) return;

    const expiry = this.getTokenExpiry(token);
    if (!expiry) return;

    const timeUntilExpiry = expiry - Date.now();

    if (timeUntilExpiry <= 0) {
      // Token already expired
      this.logout('Your session has expired. Please sign in again.');
      return;
    }

    // Schedule logout slightly before expiry (5 seconds buffer)
    const delay = Math.max(timeUntilExpiry - 5000, 0);

    this.ngZone.runOutsideAngular(() => {
      this.tokenExpiryTimer = setTimeout(() => {
        this.ngZone.run(() => {
          this.logout('Your session has expired. Please sign in again.');
        });
      }, delay);
    });
  }

  private clearExpiryTimer() {
    if (this.tokenExpiryTimer) {
      clearTimeout(this.tokenExpiryTimer);
      this.tokenExpiryTimer = null;
    }
  }
}
