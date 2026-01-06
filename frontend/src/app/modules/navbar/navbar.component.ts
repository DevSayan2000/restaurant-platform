import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';
import { ButtonModule } from 'primeng/button';
import { ThemeService } from 'app/core/services/theme.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  imports: [CommonModule, ButtonModule, RouterModule]
})
export class NavbarComponent {
  constructor(
    public auth: AuthService,
    public theme: ThemeService
  ) {}

  logout() {
    this.auth.logout();
  }

  toggleTheme() {
    this.theme.toggleTheme();
  }
}
