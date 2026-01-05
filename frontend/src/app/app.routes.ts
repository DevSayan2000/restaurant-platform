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
    ],
  },
];
