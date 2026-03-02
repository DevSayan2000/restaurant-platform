import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { Router, RouterModule } from '@angular/router';
import { UserApiService, UserLoginPayload } from 'app/core/services/user-api.service';

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
export class SignInComponent {
  loginForm: FormGroup;
  submitted = false;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private userApiService: UserApiService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
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
        this.userApiService.getLoggedInUser().subscribe(()=> {
          this.isLoading = false;
          this.router.navigate(['/dashboard'])
        });
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }
}
