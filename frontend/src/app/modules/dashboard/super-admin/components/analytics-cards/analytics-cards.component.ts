import { Component, Input } from '@angular/core';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-analytics-cards',
  standalone: true,
  templateUrl: './analytics-cards.component.html',
  imports: [CardModule],
})
export class AnalyticsCardsComponent {
  @Input() totalRestaurants: number = 0;
  @Input() totalUsers: number = 0;
  @Input() avgRating: number = 0;
}
