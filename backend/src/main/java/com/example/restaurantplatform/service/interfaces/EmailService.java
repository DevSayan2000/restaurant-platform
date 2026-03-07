package com.example.restaurantplatform.service.interfaces;

/**
 * Service for sending email notifications.
 */
public interface EmailService {

    /**
     * Sends a verification OTP email to a newly registered user.
     * This method is executed asynchronously.
     *
     * @param toEmail  the recipient's email address
     * @param userName the user's display name
     * @param otp      the 6-digit verification code
     */
    void sendVerificationOtp(String toEmail, String userName, String otp);

    /**
     * Sends a welcome email after the user has verified their email.
     * This method is executed asynchronously.
     *
     * @param toEmail  the recipient's email address
     * @param userName the user's display name
     */
    void sendWelcomeEmail(String toEmail, String userName);
}


