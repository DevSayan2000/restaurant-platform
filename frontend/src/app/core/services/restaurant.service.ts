import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject, retry, takeUntil, timer } from 'rxjs';
import { Restaurant, RestaurantApiService, RestaurantReview } from './restaurant-api.service';
import { MessageService } from 'primeng/api';
import { Analytics, AnalyticsApiService } from './analytic-api.service';

@Injectable({
  providedIn: 'root',
})
export class RestaurantService {
  private restaurantsSubject = new BehaviorSubject<Restaurant[]>([]);
  restaurants$ = this.restaurantsSubject.asObservable();

  private analyticsSubject = new BehaviorSubject<Analytics | null>(null);
  analytics$ = this.analyticsSubject.asObservable();

  private recentReviewsSubject = new BehaviorSubject<RestaurantReview[]>([]);
  recentReviews$ = this.recentReviewsSubject.asObservable();

  private cancel$ = new Subject<void>();

  constructor(
    private restaurantApiService: RestaurantApiService,
    private messageService: MessageService,
    private analyticApiService: AnalyticsApiService,
  ) {}

  /** Get current restaurants */
  getRestaurants() {
    return this.restaurantsSubject.value;
  }

  /** Delete restaurant */
  deleteRestaurant(id: number) {
    this.restaurantApiService.deleteRestaurant(id).pipe(takeUntil(this.cancel$)).subscribe({
      next: (response) => {
        const filtered = this.restaurantsSubject.value.filter((r) => r.id !== id);
        this.restaurantsSubject.next(filtered);
        this.getAnalytics();

        this.messageService.add({
          severity: 'success',
          summary: 'Restaurant Deleted',
          detail: response.message,
        });
      },
    });
  }

  /** Clear all restaurants and cancel pending requests */
  clearAll() {
    this.cancel$.next();
    this.restaurantsSubject.next([]);
    this.analyticsSubject.next(null);
    this.recentReviewsSubject.next([]);
  }

  refresh() {
    this.cancel$.next();

    this.restaurantApiService.getRestaurants().pipe(
      retry({ count: 1, delay: () => timer(1000) }),
      takeUntil(this.cancel$),
    ).subscribe({
      next: (response) => {
        this.restaurantsSubject.next(response.restaurantResponses);
      },
    });

    this.getAnalytics();
  }

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

  getRecentReviews() {
    this.analyticApiService.getRecentReviews().pipe(
      retry({ count: 1, delay: () => timer(1000) }),
      takeUntil(this.cancel$),
    ).subscribe({
      next: (response) => {
        this.recentReviewsSubject.next(response.reviews);
      },
    });
  }
}
