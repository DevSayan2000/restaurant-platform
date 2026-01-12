import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { LoadingBarComponent } from './modules/loading-bar/loading-bar.component';
import { ToastModule } from 'primeng/toast';
import { NavbarComponent } from './modules/navbar/navbar.component';
import { UserApiService } from './core/services/user-api.service';
import { ConfirmationDialogComponent } from './modules/shared/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ButtonModule, LoadingBarComponent, ToastModule, NavbarComponent, ConfirmationDialogComponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  constructor(private userApiService: UserApiService) {}
  ngOnInit() {
    const token = localStorage.getItem('token');
    if (token) {
      this.userApiService.getLoggedInUser().subscribe();
    }
  }
}
