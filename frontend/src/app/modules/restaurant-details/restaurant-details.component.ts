import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { RestaurantInfoCardComponent } from './components/restaurant-info-card/restaurant-info-card.component';
import { ReviewItemComponent } from './components/review-item/review-item.component';
import { ReviewFormComponent } from './components/review-form/review-form.component';
import { MenuSectionComponent } from './components/menu-section/menu-section.component';
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
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-restaurant-details',
  standalone: true,
  imports: [
    CommonModule,
    DialogModule,
    RestaurantInfoCardComponent,
    ReviewItemComponent,
    ReviewFormComponent,
    MenuSectionComponent,
    ButtonModule,
  ],
  templateUrl: './restaurant-details.component.html',
})
export class RestaurantDetailsComponent implements OnDestroy {
  restaurant: Restaurant | null = null;
  role = Role;
  user: User | null = null;

  reviews: RestaurantReview[] = [];

  showAddReviewDialog = false;
  restaurantId: string = '';
  maxResults: number = 0;

  private destroy$ = new Subject<void>();

  constructor(
    private restaurantApiService: RestaurantApiService,
    private route: ActivatedRoute,
    private auth: AuthService,
    private messageService: MessageService
  ) {
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      this.restaurantId = params['restaurantId'];
      this.getRestaurantDetails();
      this.getRestaurantReviews();
    });
    this.auth.user$.pipe(takeUntil(this.destroy$)).subscribe((value) => {
      this.user = value;
    });
  }

  get isMenuOwner(): boolean {
    if (!this.user || !this.restaurant) return false;
    return this.restaurant.isOwner === true;
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  addReview(data: any) {
    const payload = {
      rating: data.rating,
      review: data.review,
    };
    this.restaurantApiService.addRestaurantReview(this.restaurantId, payload).pipe(takeUntil(this.destroy$)).subscribe({
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
    this.restaurantApiService.deleteRestaurantReview(this.restaurantId).pipe(takeUntil(this.destroy$)).subscribe({
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
    this.restaurantApiService.getRestaurantDetails(this.restaurantId).pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => (this.restaurant = response),
    });
  }

  getRestaurantReviews() {
    this.restaurantApiService.getRestaurantReviews(this.restaurantId).pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => {
        this.reviews = response.reviews;
        this.maxResults = response.maxResults;
      },
    });
  }
}
