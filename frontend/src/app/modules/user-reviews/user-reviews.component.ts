import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { UserApiService, UserReview } from 'app/core/services/user-api.service';
import { TableModule } from 'primeng/table';
import { TruncateTextComponent } from '../shared/truncate-text/truncate-text.component';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-user-reviews',
  templateUrl: './user-reviews.component.html',
  imports: [
    CommonModule,
    ButtonModule,
    RouterModule,
    CardModule,
    TagModule,
    TableModule,
    TruncateTextComponent,
  ],
})
export class UserReviewsComponent implements OnInit, OnDestroy {
  userReviews: UserReview[] = [];
  private destroy$ = new Subject<void>();

  constructor(private userApiService: UserApiService) {}

  ngOnInit() {
    this.userApiService.userReviews().pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => {
        this.userReviews = response.reviews;
      },
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
