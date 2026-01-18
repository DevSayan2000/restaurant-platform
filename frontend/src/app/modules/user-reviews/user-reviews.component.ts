import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { UserApiService, UserReview } from 'app/core/services/user-api.service';
import { TableModule } from 'primeng/table';
import { TruncateTextComponent } from '../shared/truncate-text/truncate-text.component';

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
export class UserReviewsComponent {
  userReviews: UserReview[] = [];

  constructor(private userApiService: UserApiService) {
    this.userApiService.userReviews().subscribe({
      next: (response) => {
        this.userReviews = response.reviews;
      },
    });
  }
}
