import { Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MessageService } from 'primeng/api';
import { UserApiService } from 'app/core/services/user-api.service';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    ButtonModule,
    CardModule,
    RouterModule,
  ],
})
export class VerifyEmailComponent implements OnInit, OnDestroy {
  email = '';
  otp = '';
  isVerifying = false;
  isResending = false;
  errorMessage = '';
  resendCountdown = 0;
  private countdownInterval: ReturnType<typeof setInterval> | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userApiService: UserApiService,
    private messageService: MessageService,
    private ngZone: NgZone
  ) {}

  ngOnInit() {
    this.email = this.route.snapshot.queryParams['email'] || '';
    if (!this.email) {
      this.router.navigateByUrl('/sign-up');
    }
    this.startResendCountdown();
  }

  ngOnDestroy() {
    if (this.countdownInterval) {
      clearInterval(this.countdownInterval);
    }
  }

  verify() {
    if (this.otp.length !== 6 || this.isVerifying) return;

    this.isVerifying = true;
    this.errorMessage = '';

    this.userApiService.verifyEmail(this.email, this.otp).subscribe({
      next: (res) => {
        this.isVerifying = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Verified!',
          detail: res.message,
        });
        this.router.navigateByUrl('/sign-in');
      },
      error: (err) => {
        this.isVerifying = false;
        this.errorMessage = err?.error?.errorMessage || 'Verification failed. Please try again.';
      },
    });
  }

  resend() {
    if (this.isResending || this.resendCountdown > 0) return;

    this.isResending = true;
    this.errorMessage = '';

    this.userApiService.resendOtp(this.email).subscribe({
      next: (res) => {
        this.isResending = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Sent!',
          detail: res.message,
        });
        this.startResendCountdown();
      },
      error: (err) => {
        this.isResending = false;
        this.errorMessage = err?.error?.errorMessage || 'Failed to resend code. Please try again.';
      },
    });
  }

  private startResendCountdown() {
    this.resendCountdown = 60;
    if (this.countdownInterval) {
      clearInterval(this.countdownInterval);
    }
    this.ngZone.runOutsideAngular(() => {
      this.countdownInterval = setInterval(() => {
        this.ngZone.run(() => {
          this.resendCountdown--;
          if (this.resendCountdown <= 0) {
            if (this.countdownInterval) {
              clearInterval(this.countdownInterval);
              this.countdownInterval = null;
            }
          }
        });
      }, 1000);
    });
  }
}

