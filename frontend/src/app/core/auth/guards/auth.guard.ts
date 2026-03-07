import { inject } from '@angular/core';
import { CanActivateChildFn, CanActivateFn, Router } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';
import { of } from 'rxjs';

export const AuthGuard: CanActivateFn | CanActivateChildFn = (route, state) => {
  const router: Router = inject(Router);
  const authService = inject(AuthService);

  // isLoggedIn now also checks token expiry and auto-logouts if expired
  if (!authService.isLoggedIn) {
    // Redirect to the sign-in page with a redirectUrl param
    const redirectURL = state.url === '/sign-out' ? '' : `redirectURL=${state.url}`;
    const urlTree = router.parseUrl(`sign-in?${redirectURL}`);

    return of(urlTree);
  }

  // Allow the access
  return of(true);
};
