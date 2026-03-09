import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Restaurant, RestaurantReview } from 'app/core/services/restaurant-api.service';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { RippleModule } from 'primeng/ripple';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { CreateRestaurantComponent } from './create-restaurant/create-restaurant.component';
import { RestaurantService } from 'app/core/services/restaurant.service';
import { ConfirmationService } from 'app/core/services/confirmation.service';
import { Subject, takeUntil } from 'rxjs';
import { TruncateTextComponent } from 'app/modules/shared/truncate-text/truncate-text.component';
import { Analytics } from 'app/core/services/analytic-api.service';
import { UpdateRestaurantComponent } from './update-restaurant/update-restaurant.component';
import { TooltipModule } from 'primeng/tooltip';

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
    CardModule,
    CreateRestaurantComponent,
    TruncateTextComponent,
    RouterModule,
    UpdateRestaurantComponent,
    TooltipModule
  ],
})
export class AdminDashboardComponent {
  restaurants: Restaurant[] = [];
  recentReviews: RestaurantReview[] = [];
  analytics: Analytics | null = null;
  isNewUser = false;

  @ViewChild(CreateRestaurantComponent) restaurantDialog!: CreateRestaurantComponent;
  private destroy$ = new Subject<void>();

  @ViewChild(UpdateRestaurantComponent)
  updateDialog!: UpdateRestaurantComponent;

  constructor(
    private restaurantService: RestaurantService,
    private confirm: ConfirmationService,
  ) {
    this.restaurantService.restaurants$.pipe(takeUntil(this.destroy$)).subscribe((value) => {
      this.restaurants = value;
    });

    this.restaurantService.analytics$.pipe(takeUntil(this.destroy$)).subscribe((value) => {
      this.analytics = value;
    });

    this.restaurantService.recentReviews$.pipe(takeUntil(this.destroy$)).subscribe((value) => {
      this.recentReviews = value;
    });
  }

  ngOnInit() {
    // Check if this is a first-time user (just signed up)
    this.isNewUser = sessionStorage.getItem('isNewUser') === 'true';
    if (this.isNewUser) {
      sessionStorage.removeItem('isNewUser');
    }

    this.restaurantService.refresh();
    this.restaurantService.getRecentReviews();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  editRestaurant(r: Restaurant) {
    this.updateDialog.restaurant = r;
    this.updateDialog.visible = true;
  }

  deleteRestaurant(r: Restaurant) {
    this.confirm
      .confirmDelete('Delete Restaurant?', 'Are you sure you want to delete this restaurant?')
      .subscribe((yes) => {
        if (yes) {
          this.restaurantService.deleteRestaurant(r.id);
        }
      });
  }
}
