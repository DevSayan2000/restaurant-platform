import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-stats-cards',
  standalone: true,
  imports: [CommonModule, CardModule, RouterModule],
  templateUrl: './stats-cards.component.html'
})
export class StatsCardsComponent {

  @Input() totalRestaurants: number = 0;
  @Input() userReviews: number = 0;
  @Input() avgRating: number = 0;

}
