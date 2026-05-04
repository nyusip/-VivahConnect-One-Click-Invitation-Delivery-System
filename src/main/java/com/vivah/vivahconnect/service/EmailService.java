package com.vivah.vivahconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    // Spring uses this bean to send email through the SMTP settings in application.properties.
    @Autowired
    private JavaMailSender mailSender;

    // Sender email is read from configuration so it can be changed without code edits.
    @Value("${spring.mail.username}")
    private String fromEmail;

    // Sends the wedding invitation email with the uploaded invitation file attached.
    public void sendEmailWithAttachment(String guestName, String toEmail, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalStateException("Invitation file does not exist: " + filePath);
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // Fall back to a friendly name if the guest name is missing.
            String safeGuestName = (guestName == null || guestName.isBlank()) ? "Dear Family Member" : guestName;
            String htmlBody = buildInvitationMessage(safeGuestName);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("With love, we invite you to our wedding celebration");
            helper.setText(htmlBody, true);

            FileSystemResource attachment = new FileSystemResource(file);
            helper.addAttachment(attachment.getFilename(), attachment);

            mailSender.send(message);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to send email to " + toEmail + ": " + e.getMessage(), e);
        }
    }

    // Builds the styled HTML email body used for invitation delivery.
    private String buildInvitationMessage(String guestName) {
        return """
            <div style="margin:0;padding:28px;background:#fff8f1;font-family:Georgia,'Times New Roman',serif;color:#3a241f;">
                <div style="max-width:640px;margin:0 auto;background:#ffffff;border:1px solid #f1d9cf;border-radius:24px;overflow:hidden;">
                    <div style="padding:34px 38px;background:linear-gradient(135deg,#8f2d56,#b04c77);color:#fffaf5;text-align:center;">
                        <div style="font-size:12px;letter-spacing:3px;text-transform:uppercase;opacity:0.9;">VivahConnect</div>
                        <h1 style="margin:14px 0 8px;font-size:34px;line-height:1.1;">A Wedding Invitation</h1>
                        <p style="margin:0;font-size:16px;line-height:1.7;color:#fff0e7;">A joyful celebration is incomplete without the warmth of family.</p>
                    </div>
                    <div style="padding:34px 38px;">
                        <p style="margin:0 0 18px;font-size:18px;">Dear %s,</p>
                        <p style="margin:0 0 14px;font-size:16px;line-height:1.8;">
                            With love and happiness in our hearts, we are delighted to invite you to be part of our wedding celebration.
                        </p>
                        <p style="margin:0 0 14px;font-size:16px;line-height:1.8;">
                            Your presence, blessings, and warm wishes would mean so much to our family as we begin this beautiful new journey together.
                        </p>
                        <p style="margin:0 0 14px;font-size:16px;line-height:1.8;">
                            We have attached the invitation card with this email and would be truly happy to celebrate this special day with you.
                        </p>
                        <div style="margin:24px 0;padding:18px 20px;border-radius:18px;background:#fbf1ea;border:1px solid #efd7cb;">
                            <p style="margin:0;font-size:15px;line-height:1.8;color:#6a4a3f;">
                                Please keep us in your blessings and join us in making this occasion memorable for the whole family.
                            </p>
                        </div>
                        <p style="margin:0;font-size:16px;line-height:1.8;">
                            With warm regards,<br>
                            <strong>The Wedding Family</strong>
                        </p>
                    </div>
                </div>
            </div>
            """.formatted(guestName);
    }

    // Sends a plain text email, mainly useful for quick mail testing.
    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
