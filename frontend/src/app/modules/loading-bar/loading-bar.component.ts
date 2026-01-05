import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { LoadingService } from 'app/core/services/loading.service';

@Component({
  selector: 'app-loading-bar',
  standalone: true,
  imports: [CommonModule, ProgressSpinnerModule],
  templateUrl: './loading-bar.component.html',
})
export class LoadingBarComponent {
  loadingService = inject(LoadingService);
  loading$ = this.loadingService.loading$;
}
