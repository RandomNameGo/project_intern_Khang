package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class SmsServiceImpl implements SmsService {
    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @Override
    public void sendSms(String toPhoneNumber, String messageBody) {
        Message.creator(
            new PhoneNumber(toPhoneNumber),
            new PhoneNumber(fromPhoneNumber),
            messageBody
    ).create();
    }
}
