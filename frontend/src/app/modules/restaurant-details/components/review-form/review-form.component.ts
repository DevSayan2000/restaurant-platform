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

  @Output() submitReview = new EventEmitter<any>();
  @Output() cancel = new EventEmitter<void>();

  submit() {
    if (!this.rating || !this.reviewText.trim()) return;

    this.submitReview.emit({
      rating: this.rating,
      review: this.reviewText
    });

    // Reset fields
    this.rating = 0;
    this.reviewText = '';
  }
}
