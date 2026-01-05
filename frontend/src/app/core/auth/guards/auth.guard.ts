import { inject } from '@angular/core';
import { CanActivateChildFn, CanActivateFn, Router } from '@angular/router';
import { of } from 'rxjs';

export const AuthGuard: CanActivateFn | CanActivateChildFn = (route, state) => {
  const router: Router = inject(Router);
  const token = localStorage.getItem('token');

  // If the user is not authenticated...
  if (!token) {
    // Redirect to the sign-in page with a redirectUrl param
    const redirectURL = state.url === '/sign-out' ? '' : `redirectURL=${state.url}`;
    const urlTree = router.parseUrl(`sign-in?${redirectURL}`);
    // const urlTree = router.parseUrl(urlTree);

    return of(urlTree);
  }

  // Allow the access
  return of(true);
};
