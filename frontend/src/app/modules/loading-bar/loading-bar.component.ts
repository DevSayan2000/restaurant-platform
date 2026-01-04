import { Component, inject } from '@angular/core';
import { LoadingService } from 'app/core/services/loading.service';
import { CommonModule } from '@angular/common';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-loading-bar',
  standalone: true,
  imports: [CommonModule, ProgressSpinnerModule],
  templateUrl: './loading-bar.component.html',
})
export class LoadingBarComponent {
  show: boolean = false;
  private _unsubscribeAll: Subject<any> = new Subject<any>();

  constructor(private loadingService: LoadingService) {
    this.loadingService.loading$.pipe(takeUntil(this._unsubscribeAll)).subscribe((value) => {
      this.show = value;
    });
  }
}
