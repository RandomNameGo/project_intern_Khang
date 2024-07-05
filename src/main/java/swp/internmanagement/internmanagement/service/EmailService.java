package swp.internmanagement.internmanagement.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(String to, String subject, Map<String, Object> templateModel) {
        try {
            Context context = new Context();
            context.setVariables(templateModel);

            String htmlContent = templateEngine.process("verificationEmail", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            helper.addInline("logoImage", new ClassPathResource("static/images/logo.png"));
            helper.addInline("CongratuationGif", new ClassPathResource("static/images/Congratuation.gif"));
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailForgotPassword(String to, String subject, Map<String, Object> templateModel) {
        try {
            Context context = new Context();
            context.setVariables(templateModel);

            String htmlContent = templateEngine.process("forgotPassword", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            helper.addInline("logoImage", new ClassPathResource("static/images/logo.png"));
            helper.addInline("forgotGif", new ClassPathResource("static/images/forgot.gif"));
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendEmailSchedule(String to, String subject, Map<String, Object> templateModel) {
        try {
            Context context = new Context();
            context.setVariables(templateModel);

            String htmlContent = templateEngine.process("schedule", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            helper.addInline("logoImage", new ClassPathResource("static/images/logo.png"));
            helper.addInline("CongratuationGif", new ClassPathResource("static/images/Congratuation.gif"));
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     public void sendVerificationEmail(String to, String subject, Map<String, Object> templateModel) throws MessagingException {
        Context context = new Context();
        context.setVariables(templateModel);

        String htmlContent = templateEngine.process("verfiyJobEmail", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        helper.addInline("logo", new ClassPathResource("static/images/mail.jpg"));
        helper.addInline("facebook", new ClassPathResource("static/images/Facebook_Logo.png"));
        helper.addInline("twitter", new ClassPathResource("static/images/twitter.png"));
        // helper.addInline("linkedin", new ClassPathResource("static/linkedin.png"));
        // helper.addInline("instagram", new ClassPathResource("static/instagram.png"));

        javaMailSender.send(message);
    }

    public void sendEmailReject(String to, String subject, Map<String, Object> templateModel) {
        try {
            Context context = new Context();
            context.setVariables(templateModel);

            String htmlContent = templateEngine.process("rejectJobApplication", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendCertificate(String to, byte[] pdfData) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Certificate of Completion");
            helper.setText("Congratulations on completing intern time!");
            ByteArrayResource pdfResource = new ByteArrayResource(pdfData);
            helper.addAttachment("certificate.pdf", pdfResource);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
