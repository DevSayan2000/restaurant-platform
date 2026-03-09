import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Restaurant, RestaurantApiService } from 'app/core/services/restaurant-api.service';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { TagModule } from 'primeng/tag';
import { StatsCardsComponent } from './components/stats-cards/stats-cards.component';
import { RestaurantGridComponent } from './components/restaurant-grid/restaurant-grid.component';
import { Analytics, AnalyticsApiService } from 'app/core/services/analytic-api.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  imports: [
    CommonModule,
    ButtonModule,
    RouterModule,
    CardModule,
    TagModule,
    ButtonModule,
    DividerModule,
    StatsCardsComponent,
    RestaurantGridComponent,
  ],
})
export class UserDashboardComponent implements OnInit, OnDestroy {
  restaurants: Restaurant[] = [];
  analytics: Analytics | null = null;
  popularRestaurants: Restaurant[] = [];
  isNewUser = false;

  private destroy$ = new Subject<void>();

  constructor(
    private restaurantApiService: RestaurantApiService,
    private analyticApiService: AnalyticsApiService,
  ) {}

  ngOnInit() {
    // Check if this is a first-time user (just signed up)
    this.isNewUser = sessionStorage.getItem('isNewUser') === 'true';
    if (this.isNewUser) {
      sessionStorage.removeItem('isNewUser');
    }
    this.restaurantApiService.getUserRestaurants().pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => {
        this.restaurants = response.restaurantResponses;
      },
    });

    this.analyticApiService.getAnalytics().pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => {
        this.analytics = response;
      },
    });

    this.analyticApiService.getPopularRestaurants().pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => {
        this.popularRestaurants = response.restaurants;
      },
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
