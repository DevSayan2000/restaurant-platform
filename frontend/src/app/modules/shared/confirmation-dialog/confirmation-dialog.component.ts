import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ConfirmationService, ConfirmOptions } from 'app/core/services/confirmation.service';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  imports: [ButtonModule, DialogModule, CommonModule],
})
export class ConfirmationDialogComponent {
  visible = false;
  options: ConfirmOptions = {};
  private destroy$ = new Subject<void>();

  constructor(private confirmService: ConfirmationService) {
    this.confirmService.options$.pipe(takeUntil(this.destroy$)).subscribe((opts) => {
      this.options = opts;
      this.visible = true;
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  accept() {
    this.confirmService.accept();
    this.visible = false;
  }

  reject() {
    this.confirmService.reject();
    this.visible = false;
  }

  get icon() {
    switch (this.options.type) {
      case 'delete':
        return 'pi pi-trash text-red-500';
      case 'warning':
        return 'pi pi-exclamation-triangle text-amber-500';
      case 'success':
        return 'pi pi-check-circle text-green-500';
      default:
        return 'pi pi-info-circle text-blue-500';
    }
  }

  get severity(): any {
    switch (this.options.type) {
      case 'delete':
        return 'danger';
      case 'warning':
        return 'warn';
      case 'success':
        return 'success';
      case 'info':
      default:
        return 'info';
    }
  }
}
