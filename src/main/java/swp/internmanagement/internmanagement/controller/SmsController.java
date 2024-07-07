package swp.internmanagement.internmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import swp.internmanagement.internmanagement.service.SmsService;

@RestController
@RequestMapping("/internbridge/auth/")
public class SmsController {
    @Autowired
    private SmsService smsService;

    @PostMapping("/send")
    public String sendSms(
        @RequestParam("toNumber") String toNumber,
        @RequestParam("messageBody") String messageBody
    ) {
        String phoneNumber = "+" + toNumber;
        System.out.println(phoneNumber);
        smsService.sendSms("+84961674823", messageBody);
        return "SMS sent successfully!";
    }
}
