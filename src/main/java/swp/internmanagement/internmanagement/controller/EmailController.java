package swp.internmanagement.internmanagement.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
        templateModel.put("applicantName", "John");
        templateModel.put("positionName", "IT");
        templateModel.put("senderName", "Duy Anh");
        templateModel.put("companyName", "fpt");
        templateModel.put("senderPosition", "manager");
        templateModel.put("contactInformation", "19123");        
        try {
            emailService.sendEmailReject("anhtdse184413@fpt.edu.vn", "Reject", templateModel);
            return "Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email";
        }
        
    }

    
}
