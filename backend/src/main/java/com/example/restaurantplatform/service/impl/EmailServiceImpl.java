package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.service.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Brevo (formerly Sendinblue) HTTP API implementation of {@link EmailService}.
 * <p>
 * Uses Brevo's HTTPS-based API (port 443) instead of SMTP (port 587),
 * which is required for Render's free tier where outbound SMTP is blocked.
 * <p>
 * Brevo free tier: 300 emails/day — no expiry, no domain verification needed.
 * <p>
 * Emails are sent asynchronously via {@code @Async} so that the
 * user-creation response is never delayed by email latency.
 * If sending fails, the error is logged but never propagated —
 * user creation always succeeds regardless of email delivery.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Value("${app.brevo.api-key:}")
    private String brevoApiKey;

    @Value("${app.brevo.from-email:}")
    private String fromEmail;

    @Value("${app.brevo.from-name:Restaurant Platform}")
    private String fromName;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Async
    @Override
    public void sendVerificationOtp(String toEmail, String userName, String otp) {
        if (!mailEnabled) {
            log.debug("Mail is disabled — skipping verification OTP email to {}", toEmail);
            return;
        }

        if (!isConfigured()) {
            log.warn("Brevo is not configured — skipping verification OTP email to {}", toEmail);
            return;
        }

        sendEmail(
                toEmail,
                "Verify Your Email - Restaurant Platform",
                buildOtpEmailBody(userName, otp)
        );
    }

    @Async
    @Override
    public void sendWelcomeEmail(String toEmail, String userName) {
        if (!mailEnabled) {
            log.debug("Mail is disabled — skipping welcome email to {}", toEmail);
            return;
        }

        if (!isConfigured()) {
            log.warn("Brevo is not configured — skipping welcome email to {}", toEmail);
            return;
        }

        sendEmail(
                toEmail,
                "Welcome to Restaurant Platform!",
                buildWelcomeEmailBody(userName)
        );
    }

    /**
     * Sends an email using Brevo's HTTP API (HTTPS port 443).
     * Uses Java's built-in HttpClient — no external library required.
     *
     * Brevo API docs: https://developers.brevo.com/reference/sendtransacemail
     */
    private void sendEmail(String toEmail, String subject, String textBody) {
        try {
            String jsonBody = String.format(
                    """
                    {
                      "sender": { "name": "%s", "email": "%s" },
                      "to": [{ "email": "%s" }],
                      "subject": "%s",
                      "textContent": %s
                    }
                    """,
                    escapeJson(fromName),
                    escapeJson(fromEmail),
                    escapeJson(toEmail),
                    escapeJson(subject),
                    toJsonString(textBody)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BREVO_API_URL))
                    .header("api-key", brevoApiKey)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("Email sent successfully to {} (status={})", toEmail, response.statusCode());
            } else {
                log.error("Brevo email failed to {} (status={}): {}", toEmail, response.statusCode(), response.body());
            }
        } catch (Exception e) {
            log.error("Failed to send email to {} via Brevo: {}", toEmail, e.getMessage());
        }
    }

    private boolean isConfigured() {
        return brevoApiKey != null && !brevoApiKey.isBlank()
                && fromEmail != null && !fromEmail.isBlank();
    }

    /** Escapes special characters for JSON string values. */
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                     .replace("\"", "\\\"")
                     .replace("\n", "\\n")
                     .replace("\r", "\\r")
                     .replace("\t", "\\t");
    }

    /** Converts a multi-line string to a properly quoted JSON string. */
    private String toJsonString(String value) {
        return "\"" + escapeJson(value) + "\"";
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
