import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { RestaurantCardComponent } from '../restaurant-card/restaurant-card.component';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-restaurant-grid',
  standalone: true,
  imports: [CommonModule, ButtonModule, RestaurantCardComponent, CardModule],
  templateUrl: './restaurant-grid.component.html',
})
export class RestaurantGridComponent {
  @Input() title = '';
  @Input() restaurants: any[] = [];
  @Input() showExpandButton: boolean = true;

  expanded = false;

  toggleExpand() {
    this.expanded = !this.expanded;
  }

  get visibleRestaurants() {
    return this.expanded ? this.restaurants : this.restaurants.slice(0, 3);
  }
}
