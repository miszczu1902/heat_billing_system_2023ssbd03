package pl.lodz.p.it.ssbd2023.ssbd03.util.mail;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;

import java.util.Properties;

@Stateless
public class MailSender {
    Properties properties = new Properties();
    Session session;

    @PostConstruct
    public void init() {
        properties.put("mail.smtp.host", LoadConfig.loadPropertyFromConfig("mail.smtp.host"));
        properties.put("mail.smtp.port", LoadConfig.loadPropertyFromConfig("mail.smtp.port"));
        properties.put("mail.smtp.starttls.enable", LoadConfig.loadPropertyFromConfig("mail.smtp.starttls.enable"));
        properties.put("mail.smtp.auth", LoadConfig.loadPropertyFromConfig("mail.smtp.auth"));

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        LoadConfig.loadPropertyFromConfig("mail.login"),
                        LoadConfig.loadPropertyFromConfig("mail.password"));
            }
        });
    }

    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mail.login"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            throw AppException.createMailNotSentException();
        }
    }
}
