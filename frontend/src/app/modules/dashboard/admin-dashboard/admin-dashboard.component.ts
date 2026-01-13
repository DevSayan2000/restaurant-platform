import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Restaurant } from 'app/core/services/restaurant-api.service';
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
    RouterModule
  ],
})
export class AdminDashboardComponent {
  reviews: any = [];
  restaurants: Restaurant[] = [];
  @ViewChild(CreateRestaurantComponent) restaurantDialog!: CreateRestaurantComponent;
  private destroy$ = new Subject<void>();

  constructor(private restaurantService: RestaurantService, private confirm: ConfirmationService) {
    this.restaurantService.restaurants$.pipe(takeUntil(this.destroy$)).subscribe((value) => {
      this.restaurants = value;
    });
  }

  ngOnInit() {
    this.restaurantService.refresh();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  editRestaurant(r: Restaurant) {
    console.log('Edit clicked', r);
    // open dialog, navigate to edit page, etc.
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
