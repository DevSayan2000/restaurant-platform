import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';
import { Restaurant, RestaurantApiService } from 'app/core/services/restaurant-api.service';
import { TruncateTextComponent } from 'app/modules/shared/truncate-text/truncate-text.component';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { TagModule } from 'primeng/tag';

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
    TruncateTextComponent
  ],
})
export class UserDashboardComponent {
  restaurants: Restaurant[] = [];
  constructor(private restaurantApiService: RestaurantApiService) {
    this.restaurantApiService.getUserRestaurants().subscribe({
      next: (response) => {
        this.restaurants = response;
      },
    });
  }
}
