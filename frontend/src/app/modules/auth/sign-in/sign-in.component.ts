import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UserApiService, UserLoginPayload } from 'app/core/services/user-api.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  imports: [
    ReactiveFormsModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    CommonModule,
    CardModule,
    RouterModule,
  ],
})
export class SignInComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private userApiService: UserApiService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  ngOnInit() {
    // Show logout message if present (from token expiry or password change)
    const logoutMessage = sessionStorage.getItem('logoutMessage');
    if (logoutMessage) {
      this.messageService.add({
        severity: 'info',
        summary: 'Session Ended',
        detail: logoutMessage,
        life: 5000,
      });
      sessionStorage.removeItem('logoutMessage');
    }
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.loginForm.invalid || this.isLoading) {
      return;
    }

    this.isLoading = true;

    const data = this.loginForm.value;

    const payload: UserLoginPayload = {
      email: data.email,
      password: data.password,
    };

    this.userApiService.userLogin(payload).subscribe({
      next: (res) => {
        const token = res.token;
        localStorage.setItem('token', token);
        const redirectURL = this.route.snapshot.queryParamMap.get('redirectURL') || '/dashboard';
        this.userApiService.getLoggedInUser().subscribe({
          next: () => {
            this.isLoading = false;
            this.router.navigateByUrl(redirectURL);
          },
          error: () => {
            this.isLoading = false;
            this.router.navigateByUrl(redirectURL);
          }
        });
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }
}
