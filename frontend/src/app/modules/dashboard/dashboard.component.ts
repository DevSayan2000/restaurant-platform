import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthService, User } from 'app/core/auth/auth.service';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { Role } from 'app/core/enums/role.enum';
import { SuperAdminDashboardComponent } from './super-admin/super-admin-dashboard/super-admin-dashboard.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  imports: [CommonModule, UserDashboardComponent, AdminDashboardComponent, SuperAdminDashboardComponent],
})
export class DashboardComponent {
  user: User | null = null;
  Role = Role;
  constructor(public auth: AuthService) {
    this.auth.user$.subscribe((value) => {
      this.user = value;
    });
  }
}
