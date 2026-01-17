import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Restaurant, RestaurantApiService } from 'app/core/services/restaurant-api.service';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { TagModule } from 'primeng/tag';
import { StatsCardsComponent } from './components/stats-cards/stats-cards.component';
import { RestaurantGridComponent } from './components/restaurant-grid/restaurant-grid.component';
import { Analytics, AnalyticsApiService } from 'app/core/services/analytic-api.service';

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
export class UserDashboardComponent {
  restaurants: Restaurant[] = [];
  analytics: Analytics | null = null;
  popularRestaurants: Restaurant[] = [];

  constructor(
    private restaurantApiService: RestaurantApiService,
    private analyticApiService: AnalyticsApiService,
  ) {
    this.restaurantApiService.getUserRestaurants().subscribe({
      next: (response) => {
        this.restaurants = response.restaurantResponses;
      },
    });

    this.analyticApiService.getAnalytics().subscribe({
      next: (response) => {
        this.analytics = response;
      },
    });

    this.analyticApiService.getPopularRestaurants().subscribe({
      next: (response) => {
        this.popularRestaurants = response.restaurants;
      },
    });
  }
}
