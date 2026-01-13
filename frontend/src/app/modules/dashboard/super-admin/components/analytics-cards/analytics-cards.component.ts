import { Component, Input } from '@angular/core';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-analytics-cards',
  standalone: true,
  templateUrl: './analytics-cards.component.html',
  imports: [CardModule],
})
export class AnalyticsCardsComponent {
  @Input() users: any[] | null = [];
  @Input() restaurants: any[] | null = [];

  get avgRating(): number {
    if (!this.restaurants?.length) return 0;

    const ratings = this.restaurants.map((r) => r.avgRating).filter((r) => r > 0);

    return ratings.length
      ? Number((ratings.reduce((a, b) => a + b, 0) / ratings.length).toFixed(1))
      : 0;
  }
}
