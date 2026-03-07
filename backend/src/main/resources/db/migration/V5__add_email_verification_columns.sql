-- Add email verification columns to users table
ALTER TABLE users ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE users ADD COLUMN verification_otp VARCHAR(6);
ALTER TABLE users ADD COLUMN otp_expires_at TIMESTAMP;

-- Mark all existing users as verified (they were created before this feature)
UPDATE users SET email_verified = true;

