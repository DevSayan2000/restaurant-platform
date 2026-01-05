import { HttpErrorResponse, HttpEvent, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';
import { MessageService } from 'primeng/api';
import { Observable, catchError, throwError } from 'rxjs';

/**
 * Intercept
 *
 * @param req
 * @param next
 */
export const authInterceptor = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  const authService = inject(AuthService);
  const messageService = inject(MessageService);
  const router = inject(Router);

  // Clone the request object
  let newReq = req.clone();

  // Request
  //
  // If the access token didn't expire, add the Authorization header.
  // We won't add the Authorization header if the access token expired.
  // This will force the server to return a "401 Unauthorized" response
  // for the protected API routes which our response interceptor will
  // catch and delete the access token from the local storage while logging
  // the user out from the app.
  const token = localStorage.getItem('token');
  if (token) {
    newReq = req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + token),
    });
  }

  // Response
  return next(newReq).pipe(
    catchError((error) => {
      // Catch "401 Unauthorized" responses
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
            authService.logout();
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
    //   if (
    //     error instanceof HttpErrorResponse &&
    //     (error.status === 401 ||
    //       error.status === 402 ||
    //       (error.status === 400 && error.error?.errornum === 404))
    //   ) {
    //     // Sign out
    //     authService.logout();
    //   }

      return throwError(error);
    })
  );
};
