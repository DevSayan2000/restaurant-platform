import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';
import { Role } from 'app/core/enums/role.enum';
import { AnalyticsService } from 'app/core/services/analytics.service';
import { RestaurantService } from 'app/core/services/restaurant.service';
import { ThemeService } from 'app/core/services/theme.service';
import { ButtonModule } from 'primeng/button';
import { UserSettingsDialogComponent } from '../shared/user-settings-dialog/user-settings-dialog.component';
import {
  UpdateUserNamePayload,
  UpdateUserPasswordPayload,
  UserApiService,
} from 'app/core/services/user-api.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  imports: [CommonModule, ButtonModule, RouterModule, UserSettingsDialogComponent],
})
export class NavbarComponent {
  constructor(
    public auth: AuthService,
    public theme: ThemeService,
    private restaurantService: RestaurantService,
    private analyticsService: AnalyticsService,
    private userApiService: UserApiService,
    private messageService: MessageService,
  ) {}

  logout() {
    this.restaurantService.clearAll();
    this.analyticsService.clearAll();
    this.auth.logout();
  }

  toggleTheme() {
    this.theme.toggleTheme();
  }

  getRoleLabel(role: Role): string {
    switch (role) {
      case Role.USER:
        return 'User';
      case Role.RESTAURANT_ADMIN:
        return 'Restaurant Admin';
      case Role.SUPER_ADMIN:
        return 'Super Admin';
      default:
        return role;
    }
  }

  getRoleClasses(role: Role): string {
    switch (role) {
      case Role.USER:
        return 'bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-200';
      case Role.RESTAURANT_ADMIN:
        return 'bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-200';
      case Role.SUPER_ADMIN:
        return 'bg-red-100 text-red-700 dark:bg-red-900 dark:text-red-200';
      default:
        return '';
    }
  }

  handleNameUpdate(newName: string) {
    this.userApiService.updateUser({ name: newName } as UpdateUserNamePayload).subscribe({
      next: () => {
        this.userApiService.getLoggedInUser().subscribe();
      },
    });
  }

  handlePasswordUpdate(data: { currentPassword: string; newPassword: string }) {
    this.userApiService.updateUser(data as UpdateUserPasswordPayload).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Password Updated',
          detail: 'Password changed successfully. Please sign in with your new password.',
        });
        // Wait briefly so the user can see the success message, then logout
        setTimeout(() => {
          this.restaurantService.clearAll();
          this.analyticsService.clearAll();
          this.auth.logout('Password changed successfully. Please sign in with your new password.');
        }, 1500);
      },
    });
  }
}
