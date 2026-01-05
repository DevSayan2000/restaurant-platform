import { inject, Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpHandlerFn,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { LoadingService } from '../services/loading.service';

// @Injectable()
// export class LoadingInterceptor implements HttpInterceptor {
//   constructor(private loadingService: LoadingService) {}

//   intercept(req: HttpRequest<unknown>,
//     next: HttpHandlerFn): Observable<HttpEvent<any>> {
//     // Skip loading if header present
//     if (req.headers.has('X-Skip-Loading')) {
//       return next(req);
//     }

//     this.loadingService.show();

//     // return next.handle(req).pipe(finalize(() => this.loadingService.hide()));
//     return next(req).pipe(
//         finalize(() => {
//             // Set the status to false if there are any errors or the request is completed
//             this.loadingService.hide();
//         })
//     );
//   }
// }

export const LoadingInterceptor = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  const loadingService = inject(LoadingService);

  // Skip loading if header present
  if (req.headers.has('X-Skip-Loading')) {
    return next(req);
  }

  // Set the loading status to true
  loadingService.show();

  return next(req).pipe(
    finalize(() => {
      // Set the status to false if there are any errors or the request is completed
      loadingService.hide();
    })
  );
};
