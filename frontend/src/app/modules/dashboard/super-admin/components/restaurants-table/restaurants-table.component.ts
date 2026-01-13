import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TruncateTooltipDirective } from 'app/core/directives/truncate-tooltip.directive';
import { ConfirmationService } from 'app/core/services/confirmation.service';
import { Restaurant } from 'app/core/services/restaurant-api.service';
import { TruncateTextComponent } from 'app/modules/shared/truncate-text/truncate-text.component';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';

@Component({
  selector: 'app-restaurants-table',
  standalone: true,
  templateUrl: './restaurants-table.component.html',
  imports: [
    CardModule,
    TableModule,
    TagModule,
    CommonModule,
    ButtonModule,
    TruncateTextComponent,
    RouterModule,
  ],
})
export class RestaurantsTableComponent {
  @Input() restaurants: Restaurant[] = [];
  @Output() delete = new EventEmitter<number>();

  constructor(private confirm: ConfirmationService) {}

  onDeleteClick(id: number) {
    this.confirm
      .confirmDelete('Delete Restaurant?', 'Are you sure you want to delete this restaurant?')
      .subscribe((yes) => {
        if (yes) {
          this.delete.emit(id);
        }
      });
  }
}
