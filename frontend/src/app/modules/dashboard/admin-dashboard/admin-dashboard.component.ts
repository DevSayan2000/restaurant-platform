import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';
import { Restaurant, RestaurantApiService } from 'app/core/services/restaurant-api.service';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { RippleModule } from 'primeng/ripple';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  imports: [
    CommonModule,
    ButtonModule,
    RouterModule,
    TableModule,
    ButtonModule,
    TagModule,
    RippleModule,
    CardModule
  ],
})
export class AdminDashboardComponent {
  reviews: any = [];
  restaurants: Restaurant[] = [];

  constructor(private restaurantApiService: RestaurantApiService) {
    this.restaurantApiService.getRestaurants().subscribe({
      next: (response) => {
        this.restaurants = response;
      },
    });
  }

  editRestaurant(r: Restaurant) {
    console.log('Edit clicked', r);
    // open dialog, navigate to edit page, etc.
  }

  deleteRestaurant(r: Restaurant) {
    console.log('Delete clicked', r);
    // open confirmation modal
  }
}
