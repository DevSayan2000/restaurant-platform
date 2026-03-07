import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-user-settings-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    DialogModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    MenuModule,
  ],
  templateUrl: './user-settings-dialog.component.html',
})
export class UserSettingsDialogComponent {
  @Input() userName: string | null = null;
  @Output() updateNameEvent = new EventEmitter<string>();
  @Output() updatePasswordEvent = new EventEmitter<{
    currentPassword: string;
    newPassword: string;
  }>();

  showPasswordDialog = false;
  showNameDialog = false;

  menuItems: MenuItem[] = [];

  passwordForm: FormGroup;

  nameForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.menuItems = [
      {
        label: 'Update Name',
        icon: 'pi pi-user-edit',
        command: () => this.openNameDialog(),
      },
      {
        label: 'Change Password',
        icon: 'pi pi-lock',
        command: () => this.openPasswordDialog(),
      },
    ];

    this.passwordForm = this.fb.group({
      currentPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmNewPassword: ['', [Validators.required]],
    }, { validators: this.passwordMatchValidator });

    this.nameForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
    });
  }

  openNameDialog() {
    this.nameForm.patchValue({ name: this.userName });
    this.showNameDialog = true;
  }

  openPasswordDialog() {
    this.passwordForm.reset();
    this.showPasswordDialog = true;
  }

  submitName() {
    if (this.nameForm.invalid) {
      this.nameForm.markAllAsTouched();
      return;
    }

    this.updateNameEvent.emit(this.nameForm.value.name!);
    this.showNameDialog = false;
  }

  passwordMatchValidator(control: AbstractControl) {
    const newPassword = control.get('newPassword')?.value;
    const confirmNewPassword = control.get('confirmNewPassword')?.value;
    return newPassword === confirmNewPassword ? null : { passwordMismatch: true };
  }

  get passwordsMatch(): boolean {
    const newPassword = this.passwordForm.get('newPassword')?.value;
    const confirmNewPassword = this.passwordForm.get('confirmNewPassword')?.value;
    return newPassword && confirmNewPassword && newPassword === confirmNewPassword;
  }

  submitPassword() {
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }

    const { currentPassword, newPassword } = this.passwordForm.value;
    this.updatePasswordEvent.emit({ currentPassword, newPassword });
    this.showPasswordDialog = false;
  }
}
