import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Restaurant } from 'app/core/services/restaurant-api.service';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { UserApiService } from 'app/core/services/user-api.service';
import { TableModule } from 'primeng/table';
import { TruncateTextComponent } from '../shared/truncate-text/truncate-text.component';
import { Subject, takeUntil } from 'rxjs';

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
export class ReviewedRestaurantsComponent implements OnInit, OnDestroy {
  reviewedRestaurants: Restaurant[] = [];
  private destroy$ = new Subject<void>();

  constructor(
    private userApiService: UserApiService
  ) {}

  ngOnInit() {
    this.userApiService.reviewedRestaurants().pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => {
        this.reviewedRestaurants = response.restaurantResponses;
      },
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
