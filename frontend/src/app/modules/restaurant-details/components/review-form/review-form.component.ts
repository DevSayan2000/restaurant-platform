import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RatingModule } from 'primeng/rating';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-review-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RatingModule, ButtonModule],
  templateUrl: './review-form.component.html',
})
export class ReviewFormComponent {
  rating: number = 0;
  reviewText: string = '';

  submitted = false;

  @Output() submitReview = new EventEmitter<any>();
  @Output() cancel = new EventEmitter<void>();

  // --- Validation Getters ---

  get isRatingInvalid(): boolean {
    return this.submitted && (!this.rating || this.rating <= 0);
  }

  get isReviewRequiredInvalid(): boolean {
    return this.submitted && !this.reviewText.trim();
  }

  get isReviewMinLengthInvalid(): boolean {
    return (
      this.submitted && this.reviewText.trim().length > 0 && this.reviewText.trim().length < 10
    );
  }

  get isFormInvalid(): boolean {
    return !this.rating || !this.reviewText.trim();
  }

  submit() {
    this.submitted = true;

    if (this.isFormInvalid) return;

    this.submitReview.emit({
      rating: this.rating,
      review: this.reviewText.trim(),
    });

    // Reset form state
    this.rating = 0;
    this.reviewText = '';
    this.submitted = false;
  }
}
