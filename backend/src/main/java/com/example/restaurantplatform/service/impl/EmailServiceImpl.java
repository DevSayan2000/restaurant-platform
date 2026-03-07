package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Gmail SMTP implementation of {@link EmailService}.
 * <p>
 * Emails are sent asynchronously via {@code @Async} so that the
 * user-creation response is never delayed by SMTP latency.
 * If sending fails, the error is logged but never propagated —
 * user creation always succeeds regardless of email delivery.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @Async
    @Override
    public void sendVerificationOtp(String toEmail, String userName, String otp) {
        if (!mailEnabled) {
            log.debug("Mail is disabled — skipping verification OTP email to {}", toEmail);
            return;
        }

        if (fromEmail == null || fromEmail.isBlank()) {
            log.warn("Mail username (from address) is not configured — skipping verification OTP email to {}", toEmail);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Verify Your Email - Restaurant Platform");
            message.setText(buildOtpEmailBody(userName, otp));

            mailSender.send(message);
            log.info("Verification OTP email sent successfully to {}", toEmail);
        } catch (MailException e) {
            log.error("Failed to send verification OTP email to {}: {}", toEmail, e.getMessage());
        }
    }

    @Async
    @Override
    public void sendWelcomeEmail(String toEmail, String userName) {
        if (!mailEnabled) {
            log.debug("Mail is disabled — skipping welcome email to {}", toEmail);
            return;
        }

        if (fromEmail == null || fromEmail.isBlank()) {
            log.warn("Mail username (from address) is not configured — skipping welcome email to {}", toEmail);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to Restaurant Platform!");
            message.setText(buildWelcomeEmailBody(userName));

            mailSender.send(message);
            log.info("Welcome email sent successfully to {}", toEmail);
        } catch (MailException e) {
            log.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
        }
    }

    private String buildWelcomeEmailBody(String userName) {
        return String.format(
                """
                Hi %s,
                
                Welcome to Restaurant Platform! 🎉
                
                Your email has been verified and your account is now fully active. You can now:
                
                • Browse and discover restaurants
                • Rate and review your dining experiences
                • Manage your restaurant (if you're a Restaurant Admin)
                
                Log in to get started: https://restaurant-platform-chi.vercel.app/sign-in
                
                Best regards,
                The Restaurant Platform Team
                """,
                userName
        );
    }

    private String buildOtpEmailBody(String userName, String otp) {
        return String.format(
                """
                Hi %s,
                
                Thank you for registering on Restaurant Platform!
                
                Your email verification code is:
                
                    %s
                
                This code will expire in 10 minutes.
                
                If you did not create this account, please ignore this email.
                
                Best regards,
                The Restaurant Platform Team
                """,
                userName, otp
        );
    }
}



