/*
package com.example.Library_Managment.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {



@Autowired
private JavaMailSender mailSender;

public void sendTestEmail() {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo("ammarasaiyed35@gmail.com");
    message.setSubject("Test Email");
    message.setText("Hello! This is a test email from Spring Boot using Gmail App Password.");
    mailSender.send(message);
    System.out.println("✅ Email sent successfully!");
}
}
*/

package com.example.Library_Managment.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTestEmailWithPdf() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = supports attachments

            helper.setTo("abc231@gmail.com");  // recipient email
            helper.setSubject("Test Mail with PDF");
            helper.setText("Hello! This is a test email from Spring Boot with a PDF attachment.");

            // Attach the PDF from resources/static folder
            // Suppose your PDF is at src/main/resources/static/Books/gatsby.pdf
            ClassPathResource pdfFile = new ClassPathResource("static/Book/Romance/Emma.pdf");
            helper.addAttachment("Emma.pdf", pdfFile);

            mailSender.send(message);

            System.out.println("✅ Email with PDF sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email with PDF: " + e.getMessage());
        }
    }
}
