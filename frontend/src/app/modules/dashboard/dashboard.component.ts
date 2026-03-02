import { CommonModule } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import { AuthService, User } from 'app/core/auth/auth.service';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { Role } from 'app/core/enums/role.enum';
import { SuperAdminDashboardComponent } from './super-admin/super-admin-dashboard/super-admin-dashboard.component';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  imports: [CommonModule, UserDashboardComponent, AdminDashboardComponent, SuperAdminDashboardComponent],
})
export class DashboardComponent implements OnDestroy {
  user: User | null = null;
  Role = Role;
  private destroy$ = new Subject<void>();

  constructor(public auth: AuthService) {
    this.auth.user$.pipe(takeUntil(this.destroy$)).subscribe((value) => {
      this.user = value;
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
