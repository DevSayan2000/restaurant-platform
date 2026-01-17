import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Analytics, AnalyticsApiService } from './analytic-api.service';

@Injectable({
  providedIn: 'root',
})
export class AnalyticsService {
  private analyticsSubject = new BehaviorSubject<Analytics | null>(null);
  analytics$ = this.analyticsSubject.asObservable();

  constructor(private analyticApiService: AnalyticsApiService) {}

  getAnalytics() {
    this.analyticApiService.getAnalytics().subscribe({
      next: (response) => {
        this.analyticsSubject.next(response);
      },
    });
  }
}
