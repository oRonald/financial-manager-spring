package br.com.financial.manager.app.service.impl;

import br.com.financial.manager.app.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendEmailToken(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Password Recovery Token");
        message.setText("User this token to recover you password: "+ token);

        mailSender.send(message);
    }

    @Override
    @Async
    public void sendEmailStatement(byte[] pdfBytes, String email) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Financial Statement Report");
            helper.setText("Hello, your Financial Statement Report is ready for you to see!");

            helper.addAttachment("Financial_Statement_Report.pdf", new ByteArrayResource(pdfBytes));

            mailSender.send(message);
        } catch(MessagingException ex){
            throw new RuntimeException(ex.getCause());
        }
    }
}
