import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { FoodType } from 'app/core/enums/food-type.enum';

@Component({
  selector: 'app-restaurant-info-card',
  standalone: true,
  imports: [CommonModule, CardModule, TagModule],
  templateUrl: './restaurant-info-card.component.html',
})
export class RestaurantInfoCardComponent {
  @Input() restaurant: any;
  getFoodTypeSeverity(type: string) {
    switch (type) {
      case FoodType.VEG:
        return 'success';
      case FoodType.JAIN:
        return 'warn';
      case FoodType.VEGAN:
        return 'info';
      case FoodType.NON_VEG:
        return 'danger';
      default:
        return 'secondary';
    }
  }
}
