import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { LoadingBarComponent } from './modules/loading-bar/loading-bar.component';
import { Toast } from 'primeng/toast';
import { NavbarComponent } from './modules/navbar/navbar.component';
import { UserApiService } from './core/services/user-api.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ButtonModule, LoadingBarComponent, Toast, NavbarComponent],
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
