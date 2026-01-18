import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Restaurant } from 'app/core/services/restaurant-api.service';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { UserApiService } from 'app/core/services/user-api.service';
import { TableModule } from 'primeng/table';
import { TruncateTextComponent } from '../shared/truncate-text/truncate-text.component';

@Component({
  selector: 'app-reviewed-restaurants',
  templateUrl: './reviewed-restaurants.component.html',
  imports: [
    CommonModule,
    ButtonModule,
    RouterModule,
    CardModule,
    TagModule,
    TableModule,
    TruncateTextComponent
  ],
})
export class ReviewedRestaurantsComponent {
  reviewedRestaurants: Restaurant[] = [];

  constructor(
    private userApiService: UserApiService
  ) {
    this.userApiService.reviewedRestaurants().subscribe({
      next: (response) => {
        this.reviewedRestaurants = response.restaurantResponses;
      },
    });
  }
}
