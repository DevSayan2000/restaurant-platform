import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { RestaurantInfoCardComponent } from './components/restaurant-info-card/restaurant-info-card.component';
import { ReviewItemComponent } from './components/review-item/review-item.component';
import { ReviewFormComponent } from './components/review-form/review-form.component';
import {
  Restaurant,
  RestaurantApiService,
  RestaurantReview,
} from 'app/core/services/restaurant-api.service';
import { ActivatedRoute } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { Role } from 'app/core/enums/role.enum';
import { AuthService, User } from 'app/core/auth/auth.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-restaurant-details',
  standalone: true,
  imports: [
    CommonModule,
    DialogModule,
    RestaurantInfoCardComponent,
    ReviewItemComponent,
    ReviewFormComponent,
    ButtonModule,
  ],
  templateUrl: './restaurant-details.component.html',
})
export class RestaurantDetailsComponent {
  restaurant: Restaurant | null = null;
  role = Role;
  user: User | null = null;

  reviews: RestaurantReview[] = [];

  showAddReviewDialog = false;
  restaurantId: string = '';

  constructor(
    private restaurantApiService: RestaurantApiService,
    private route: ActivatedRoute,
    private auth: AuthService,
    private messageService: MessageService
  ) {
    this.route.params.subscribe((params) => {
      this.restaurantId = params['restaurantId'];
      this.getRestaurantDetails();
      this.getRestaurantReviews();
    });
    this.auth.user$.subscribe((value) => {
      this.user = value;
    });
  }

  addReview(data: any) {
    const payload = {
      rating: data.rating,
      review: data.review,
    };
    this.restaurantApiService.addRestaurantReview(this.restaurantId, payload).subscribe({
      next: () => {
        this.getRestaurantReviews();
        this.getRestaurantDetails();
        this.messageService.add({
          severity: 'success',
          summary: 'Review added',
          detail: `Review added successfully`,
        });
        this.showAddReviewDialog = false;
      },
    });
  }

  deleteReview(id: number) {
    this.restaurantApiService.deleteRestaurantReview(this.restaurantId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Review deleted',
          detail: `Review deleted successfully`,
        });
        this.reviews = this.reviews.filter((r) => r.id !== id);
        this.getRestaurantDetails();
      },
    });
  }

  getRestaurantDetails() {
    this.restaurantApiService.getRestaurantDetails(this.restaurantId).subscribe({
      next: (response) => (this.restaurant = response),
    });
  }

  getRestaurantReviews() {
    this.restaurantApiService.getRestaurantReviews(this.restaurantId).subscribe({
      next: (response) => (this.reviews = response.reviews),
    });
  }
}
