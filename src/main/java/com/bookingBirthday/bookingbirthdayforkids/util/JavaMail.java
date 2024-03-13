package com.bookingBirthday.bookingbirthdayforkids.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class JavaMail {

    @Autowired
    private JavaMailSender emailSender;

        public void sendEmail(String to, String subject, String body) {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            try {
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(body, true); // true indicates HTML content

                emailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        }


}
