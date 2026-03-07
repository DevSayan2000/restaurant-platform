import { Routes } from '@angular/router';
import { NoAuthGuard } from './core/auth/guards/noAuth.guard';
import { AuthGuard } from './core/auth/guards/auth.guard';

export const routes: Routes = [
  // Redirect empty path to '/dashboard'
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  {
    path: '',
    canActivate: [NoAuthGuard],
    canActivateChild: [NoAuthGuard],
    children: [
      {
        path: 'sign-in',
        loadChildren: () => import('app/modules/auth/sign-in/sign-in.routes'),
      },
      { path: 'sign-up', loadChildren: () => import('app/modules/auth/sign-up/sign-up.routes') },
      { path: 'verify-email', loadChildren: () => import('app/modules/auth/verify-email/verify-email.routes') },
    ],
  },
  {
    path: '',
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        loadChildren: () => import('app/modules/dashboard/dashboard.routes').then((m) => m.default),
      },
      {
        path: 'restaurants',
        loadChildren: () => import('app/modules/restaurant-details/restaurant-details.routes').then((m) => m.default),
      },
      {
        path: 'reviewed-restaurants',
        loadComponent: () => import('app/modules/reviewed-restaurants/reviewed-restaurants.component').then(m => m.ReviewedRestaurantsComponent)
      },
      {
        path: 'user-reviews',
        loadComponent: () => import('app/modules/user-reviews/user-reviews.component').then(m => m.UserReviewsComponent)
      }
    ],
  },
];
