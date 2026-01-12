import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

export interface ConfirmOptions {
  title?: string;
  message?: string;
  confirmLabel?: string;
  cancelLabel?: string;
  type?: 'delete' | 'info' | 'warning' | 'success';
}

@Injectable({
  providedIn: 'root',
})
export class ConfirmationService {
  private optionsSubject = new Subject<ConfirmOptions>();
  private confirmationSubject = new Subject<boolean>();

  options$ = this.optionsSubject.asObservable();
  confirmation$ = this.confirmationSubject.asObservable();

  confirm(
    title = 'Are you sure?',
    message = 'Do you want to proceed?',
    confirmLabel = 'Yes',
    cancelLabel = 'No',
    type: ConfirmOptions['type'] = 'info'
  ): Observable<boolean> {
    return new Observable<boolean>((observer) => {
      // Show the dialog
      this.optionsSubject.next({ title, message, confirmLabel, cancelLabel, type });

      const sub = this.confirmationSubject.subscribe((value) => {
        observer.next(value);
        observer.complete(); // complete after first response
        sub.unsubscribe(); // clean up subscription
      });
    });
  }

  /** PRESET: DELETE CONFIRMATION */
  confirmDelete(title: string, message: string): Observable<boolean> {
    return this.confirm(title, message, 'Delete', 'Cancel', 'delete');
  }

  /** PRESET: INFO */
  confirmInfo(title: string, message: string): Observable<boolean> {
    return this.confirm(title, message, 'OK', undefined, 'info');
  }

  /** PRESET: WARNING */
  confirmWarning(title: string, message: string): Observable<boolean> {
    return this.confirm(title, message, 'Proceed', 'Cancel', 'warning');
  }

  /** PRESET: SUCCESS */
  confirmSuccess(title: string, message: string): Observable<boolean> {
    return this.confirm(title, message, 'OK', undefined, 'success');
  }

  accept() {
    this.confirmationSubject.next(true);
  }

  reject() {
    this.confirmationSubject.next(false);
  }
}
