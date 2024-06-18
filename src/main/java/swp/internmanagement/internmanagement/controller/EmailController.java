package swp.internmanagement.internmanagement.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swp.internmanagement.internmanagement.service.EmailService;


@RestController
@RequestMapping("/internbridge/auth/")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailController {
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/email")
    public String sendVerificationEmail() {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("verificationCode", "Click button below to activate your account");
        templateModel.put("verificationUrl", "https://example.com/verify?code=" + "a");
        try {
            emailService.sendEmailForgotPassword("hungpltse172380@fpt.edu.vn", "Verify your email", templateModel);
            return "Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email";
        }
    }
    
}
