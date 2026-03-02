import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject, retry, takeUntil, timer } from 'rxjs';
import { Analytics, AnalyticsApiService } from './analytic-api.service';

@Injectable({
  providedIn: 'root',
})
export class AnalyticsService {
  private analyticsSubject = new BehaviorSubject<Analytics | null>(null);
  analytics$ = this.analyticsSubject.asObservable();

  private cancel$ = new Subject<void>();

  constructor(private analyticApiService: AnalyticsApiService) {}

  getAnalytics() {
    this.analyticApiService.getAnalytics().pipe(
      retry({ count: 1, delay: () => timer(1000) }),
      takeUntil(this.cancel$),
    ).subscribe({
      next: (response) => {
        this.analyticsSubject.next(response);
      },
    });
  }

  clearAll() {
    this.cancel$.next();
    this.analyticsSubject.next(null);
  }
}
