package com.dentiz.dentizapi.Components.Mails.MailService;

import com.dentiz.dentizapi.Components.Mails.MailStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String dentizMail;

    public void sendMail(String mail, MailStructure mailStructure) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(dentizMail);
        message.setSubject(mailStructure.getSubject());
        message.setText(mailStructure.getBody());
        message.setTo(mail);

        javaMailSender.send(message);
    }
}
