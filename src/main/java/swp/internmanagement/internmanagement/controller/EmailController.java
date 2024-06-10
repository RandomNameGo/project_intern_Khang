package swp.internmanagement.internmanagement.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import swp.internmanagement.internmanagement.service.EmailService;


@RestController
@RequestMapping("/internbridge/auth/")
public class EmailController {
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/email")
    public String sendVerificationEmail() {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("verificationCode", "a");
        templateModel.put("verificationUrl", "https://example.com/verify?code=" + "a");
        try {
            emailService.sendEmail("anhtdse184413@fpt.edu.vn", "Verify your email", templateModel);
            return "Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email";
        }
    }
    
}
