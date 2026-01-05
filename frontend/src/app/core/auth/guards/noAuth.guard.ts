import { inject } from '@angular/core';
import { CanActivateChildFn, CanActivateFn, Router } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';
import { of } from 'rxjs';

export const NoAuthGuard: CanActivateFn | CanActivateChildFn = (route, state) => {
  const router: Router = inject(Router);
  const authService = inject(AuthService);

  if (authService.isLoggedIn) {
    return of(router.parseUrl('/dashboard'));
  }

  // Allow the access
  return of(true);
};
