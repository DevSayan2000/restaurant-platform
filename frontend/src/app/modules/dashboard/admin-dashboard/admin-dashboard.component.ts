import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  imports: [CommonModule, ButtonModule, RouterModule],
})
export class AdminDashboardComponent {
  reviews: any = [];
  constructor(public auth: AuthService) {}
}
