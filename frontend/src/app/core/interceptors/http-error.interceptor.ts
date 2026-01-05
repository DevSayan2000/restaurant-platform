import { inject } from '@angular/core';
import {
  HttpRequest,
  HttpHandlerFn,
  HttpEvent,
  HttpInterceptorFn,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';

export const httpErrorInterceptor: HttpInterceptorFn = (
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> => {
  const messageService = inject(MessageService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.error instanceof ErrorEvent) {
        // Client-side / network error
        messageService.add({
          key: 'global',
          severity: 'error',
          summary: 'Network Error',
          detail: error.error.message,
        });
      } else {
        // Backend error
        const status = error.status;
        const detail =
          error.error?.errorMessage || error.error?.message || error.message || 'Unknown error';

        switch (status) {
          case 400:
            messageService.add({
              key: 'global',
              severity: 'error',
              summary: 'Bad Request',
              detail,
            });
            break;
          case 401:
            messageService.add({
              key: 'global',
              severity: 'error',
              summary: 'Unauthorized',
              detail: 'Redirecting to login...',
            });
            router.navigate(['/sign-in']);
            break;
          case 403:
            messageService.add({
              key: 'global',
              severity: 'error',
              summary: 'Forbidden',
              detail: 'Access denied',
            });
            break;
          case 404:
            messageService.add({
              key: 'global',
              severity: 'warn',
              summary: 'Not Found',
              detail: 'Resource not found',
            });
            break;
          case 500:
            messageService.add({
              key: 'global',
              severity: 'error',
              summary: 'Server Error',
              detail: 'Internal server error',
            });
            break;
          default:
            messageService.add({
              key: 'global',
              severity: 'error',
              summary: `Error ${status}`,
              detail,
            });
        }
      }

      return throwError(() => error);
    })
  );
};
