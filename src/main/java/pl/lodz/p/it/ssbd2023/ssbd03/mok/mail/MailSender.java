package pl.lodz.p.it.ssbd2023.ssbd03.mok.mail;

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
    private Properties properties = new Properties();
    private Session session;

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

    public void sendLinkToActivateAccountToEmail(String to, String subject, String content) {
        sendEmail(to, subject, content, "activation.url");
    }
     public void sendInformationAccountDisabled(String to) {
        sendEmail(to, "Account has been disabled",
                "Dear User, \n" +
                        "Your account has been disabled", null);
     }

     public void sendInformationAccountEnabled(String to) {
         sendEmail(to, "Account has been disabled",
                 "Dear User, \n" +
                         "Your account has been enabled", null);
     }

    public void sendInformationAccountActivated(String to) {
        sendEmail(to, "Account has been disabled",
                "Dear User, \n" +
                        "Your account has been activated", null);
    }

    private void sendEmail(String to, String subject, String content, String propertyFromConfig) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(LoadConfig.loadPropertyFromConfig("mail.login")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            if(propertyFromConfig != null) {
                message.setText(LoadConfig.loadPropertyFromConfig(propertyFromConfig) + "?" + content);
            } else {
                message.setText(content);
            }
            Transport.send(message);
        } catch (MessagingException e) {
            throw AppException.createMailNotSentException();
        }
    }
}
