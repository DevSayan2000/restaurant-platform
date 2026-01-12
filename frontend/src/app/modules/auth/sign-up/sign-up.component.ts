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
  ],
})
export class SignUpComponent {
  signUpForm: FormGroup;
  submitted = false;

  accountTypes = [
    { label: 'User', value: Role.USER },
    { label: 'Restaurant Admin', value: Role.RESTAURANT_ADMIN },
  ];

  Role = Role;
  selectedRole: Role | null = null;

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
        password: ['', [Validators.required, Validators.minLength(6)]],
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

  onSubmit() {
    this.submitted = true;

    if (this.signUpForm.invalid) {
      return;
    }

    const data = this.signUpForm.value;

    const payload: CreateUserPayload = {
      name: data.name,
      email: data.email,
      password: data.password,
      role: data.role,
    };

    this.userApiService.createUser(payload).subscribe({
      next: (res) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: res.message,
        })
        this.router.navigateByUrl('/sign-in');
      },
      error: (err) => {
        console.error('ERROR RESPONSE', err);
      },
    });
  }

  selectRole(role: Role | null) {
    this.selectedRole = role;
    this.signUpForm.patchValue({ role });
  }
}
