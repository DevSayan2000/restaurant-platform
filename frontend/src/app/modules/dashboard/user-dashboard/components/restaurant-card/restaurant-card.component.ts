import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { TruncateTextComponent } from 'app/modules/shared/truncate-text/truncate-text.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-restaurant-card',
  standalone: true,
  imports: [CommonModule, TagModule, CardModule, TruncateTextComponent, RouterModule],
  templateUrl: './restaurant-card.component.html',
})
export class RestaurantCardComponent {
  @Input() data: any;

  getTagSeverity(type: string) {
    switch (type) {
      case 'VEG': return 'success';
      case 'JAIN': return 'warn';
      case 'VEGAN': return 'info';
      case 'NON_VEG': return 'danger';
      default: return 'secondary';
    }
  }
}
