import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
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
    this.restaurantApiService.deleteRestaurant(id).subscribe({
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

  /** Clear all restaurants */
  clearAll() {
    this.restaurantsSubject.next([]);
  }

  refresh() {
    this.restaurantApiService.getRestaurants().subscribe({
      next: (response) => {
        this.restaurantsSubject.next(response.restaurantResponses);
      },
    });

    this.getAnalytics();
  }

  getAnalytics() {
    this.analyticApiService.getAnalytics().subscribe({
      next: (response) => {
        this.analyticsSubject.next(response);
      },
    });
  }

  getRecentReviews() {
    this.analyticApiService.getRecentReviews().subscribe({
      next: (response) => {
        this.recentReviewsSubject.next(response.reviews);
      },
    });
  }
}
