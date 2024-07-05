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
        templateModel.put("header", "John");
        templateModel.put("message", "In the meantime, feel free to explore our <a href=\"http://www.example.com\">website</a> for more information about our services and features. We are constantly working to improve and add new functionalities to make your experience even better."); 
        try {
            emailService.sendEmailReplyReq("anhtdse184413@fpt.edu.vn", "Reject", templateModel);
            return "Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email";
        }
        
    }

    
}
