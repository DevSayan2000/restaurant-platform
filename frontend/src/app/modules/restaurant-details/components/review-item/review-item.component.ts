import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { RestaurantReview } from 'app/core/services/restaurant-api.service';

@Component({
  selector: 'app-review-item',
  standalone: true,
  imports: [CommonModule, CardModule, ButtonModule],
  templateUrl: './review-item.component.html',
})
export class ReviewItemComponent {
  @Input() review: RestaurantReview | null = null;
  @Input() canDelete: boolean = false;

  @Output() onDelete = new EventEmitter<number>();
}
