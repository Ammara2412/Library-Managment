package com.example.Library_Managment.controller;

import com.example.Library_Managment.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-test-email")
    public String sendEmail() {
        emailService.sendTestEmailWithPdf();
        return "Email sent successfully!";


    }
}
