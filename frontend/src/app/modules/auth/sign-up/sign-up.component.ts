import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { Router, RouterModule } from '@angular/router';
import { SelectButtonModule } from 'primeng/selectbutton';
import { CreateUserPayload, UserApiService } from 'app/core/services/user-api.service';
import { Role } from 'app/core/enums/role.enum';
import { MessageService } from 'primeng/api';
import { STRONG_PASSWORD_REGEX } from 'app/utils/password';
import { Divider } from 'primeng/divider';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  imports: [
    ReactiveFormsModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    CommonModule,
    CardModule,
    RouterModule,
    SelectButtonModule,
    Divider
  ],
})
export class SignUpComponent {
  signUpForm: FormGroup;
  submitted = false;
  isLoading = false;

  accountTypes = [
    { label: 'User', value: Role.USER },
    { label: 'Restaurant Admin', value: Role.RESTAURANT_ADMIN },
  ];

  Role = Role;
  selectedRole: Role | null = null;
  passwordRegex = STRONG_PASSWORD_REGEX;

  constructor(
    private fb: FormBuilder,
    private userApiService: UserApiService,
    private router: Router,
    private messageService: MessageService
  ) {
    this.signUpForm = this.fb.group(
      {
        role: ['', Validators.required],
        name: ['', [Validators.required, Validators.minLength(3)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.pattern(STRONG_PASSWORD_REGEX)]],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.passwordMatchValidator }
    );
  }

  get f() {
    return this.signUpForm.controls;
  }

  passwordMatchValidator(control: AbstractControl) {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  get passwordsMatch(): boolean {
    const password = this.signUpForm.get('password')?.value;
    const confirmPassword = this.signUpForm.get('confirmPassword')?.value;
    return password && confirmPassword && password === confirmPassword;
  }

  onSubmit() {
    this.submitted = true;

    if (this.signUpForm.invalid || this.isLoading) {
      return;
    }

    this.isLoading = true;

    const data = this.signUpForm.value;

    const payload: CreateUserPayload = {
      name: data.name,
      email: data.email,
      password: data.password,
      role: data.role,
    };

    this.userApiService.createUser(payload).subscribe({
      next: (res) => {
        this.isLoading = false;
        sessionStorage.setItem('isNewUser', 'true');
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: res.message,
        })
        this.router.navigate(['/verify-email'], {
          queryParams: { email: data.email },
        });
      },
      error: (err) => {
        this.isLoading = false;
        console.error('ERROR RESPONSE', err);
      },
    });
  }

  selectRole(role: Role | null) {
    this.selectedRole = role;
    this.signUpForm.patchValue({ role });
  }
}
