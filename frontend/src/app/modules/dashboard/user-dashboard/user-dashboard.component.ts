import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  imports: [CommonModule, ButtonModule, RouterModule],
})
export class UserDashboardComponent {
  restaurants: any = [];
  constructor(public auth: AuthService) {}
}
