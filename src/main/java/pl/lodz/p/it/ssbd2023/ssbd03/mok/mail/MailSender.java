package pl.lodz.p.it.ssbd2023.ssbd03.mok.mail;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Internationalization;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;

import java.util.Properties;

@Stateless
public class MailSender {

    @Inject
    private Internationalization internationalization;

    private static final String MAIL_SUBJECT_CONFIRMATION_LINK = "Confirm your new email";
    private static final String CHANGED_PASSWORD_BY_ADMIN_CONTENT_MESSAGE = """
            Administrator changed your password.
            Click on the link to reset your password
            %s?token=%s
            """;
    private static final String RESET_PASSWORD_CONTENT_MESSAGE = """
            Click on the link to reset your password
            %s?token=%s
            """;
    private final Properties properties = new Properties();
    private Session session;

    @PostConstruct
    public void init() {
        properties.put("mail.smtp.host", LoadConfig.loadPropertyFromConfig("mail.smtp.host"));
        properties.put("mail.smtp.port", LoadConfig.loadPropertyFromConfig("mail.smtp.port"));
        properties.put("mail.smtp.starttls.enable", LoadConfig.loadPropertyFromConfig("mail.smtp.starttls.enable"));
        properties.put("mail.smtp.auth", LoadConfig.loadPropertyFromConfig("mail.smtp.auth"));
        properties.put("activation.url", LoadConfig.loadPropertyFromConfig("activation.url"));
        properties.put("mail.activation.url", LoadConfig.loadPropertyFromConfig("mail.activation.url"));
        properties.put("reset.password.url", LoadConfig.loadPropertyFromConfig("reset.password.url"));

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        LoadConfig.loadPropertyFromConfig("mail.login"),
                        LoadConfig.loadPropertyFromConfig("mail.password"));
            }
        });
    }

    public void sendLinkToActivateAccount(String to, String subject, String content) {
        sendEmail(to, subject, properties.getProperty("activation.url") + "/" + content);
    }

    public void sendLinkToConfirmAnEmail(String to, String content) {
        sendEmail(to, MAIL_SUBJECT_CONFIRMATION_LINK, properties.getProperty("mail.activation.url") + "/" + content);
    }

    public void sendInformationAdminLoggedIn(String to, String ipAddress) {
        sendEmail(to, "Security Alert!",
                "Someone from ip address: " + ipAddress +
                        " just logged in to your account");//TODO - tu trzeba zrobić resource bundle
    }

    public void sendInformationAboutChangedPasswordByAdmin(String to, String token) {
        sendEmail(to, "Password changed!",
                CHANGED_PASSWORD_BY_ADMIN_CONTENT_MESSAGE.formatted(properties.getProperty("reset.password.url"), token));
    }

    public void sendInformationAboutResettingPassword(String to, String token) {
        sendEmail(to, "Reset password",
                RESET_PASSWORD_CONTENT_MESSAGE.formatted(properties.getProperty("reset.password.url"), token));
    }

    public void sendInformationAddingAnAccessLevel(String to, String role) {
        sendEmail(to, "Security Alert!",
                "The " + role +
                        " access level has been added to your account.");//TODO - tu trzeba zrobić resource bundle
    }

    public void sendInformationRevokeAnAccessLevel(String to, String role) {
        sendEmail(to, "Security Alert!",
                "The " + role +
                        " access level has been removed from your account.");//TODO - tu trzeba zrobić resource bundle
    }

    public void sendInformationAccountDisabled(String to, String language) {
        sendEmail(to, internationalization.getMessage("mail.account.disabled.title", language),
                internationalization.getMessage("mail.account.disabled.message", language));
    }

    public void sendInformationAccountEnabled(String to, String language) {
        sendEmail(to, internationalization.getMessage("mail.account.enabled.title", language),
                internationalization.getMessage("mail.account.enabled.message", language));
    }

    public void sendInformationAccountActivated(String to, String language) {
        sendEmail(to, internationalization.getMessage("mail.account.activated.title", language),
                internationalization.getMessage("mail.account.activated.message", language));
    }

    public void sendReminderAboutAccountConfirmation(String to, String activationToken) {
        sendEmail(to, "Account is waiting for activation",
                "Dear User, \n" +
                        "Your account is waiting for activation. \n" +
                        "Activation link: " + properties.getProperty("activation.url") + activationToken);
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(LoadConfig.loadPropertyFromConfig("mail.login")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            throw AppException.createMailNotSentException();
        }
    }
}