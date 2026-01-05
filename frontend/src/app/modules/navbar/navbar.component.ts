import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from 'app/core/auth/auth.service';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  imports: [CommonModule, ButtonModule, RouterModule]
})
export class NavbarComponent {
  constructor(public auth: AuthService) {
    this.auth.user$.subscribe((value)=> {
      console.log(value);
    })
  }

  logout() {
    this.auth.logout();
  }
}
