package pl.lodz.p.it.ssbd2023.ssbd03.mok.mail;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.inject.Inject;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Internationalization;

import java.util.Properties;

@ApplicationScoped
public class MailSender {
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

    @Inject
    Internationalization internationalization;

    @Inject
    @ConfigProperty(name = "mail.smtp.host")
    String mailSmtpHost;

    @Inject
    @ConfigProperty(name = "mail.smtp.host")
    String mailSmtpPort;

    @Inject
    @ConfigProperty(name = "mail.smtp.starttls.enable")
    String tlsEnable;

    @Inject
    @ConfigProperty(name = "mail.smtp.auth")
    String smtpAuth;

    @Inject
    @ConfigProperty(name = "activation.url")
    String url;

    @Inject
    @ConfigProperty(name = "mail.activation.url")
    String mailUrl;

    @Inject
    @ConfigProperty(name = "reset.password.url")
    String resetUrl;

    @Inject
    @ConfigProperty(name = "mail.login")
    String login;

    @Inject
    @ConfigProperty(name = "mail.password")
    String password;


    @PostConstruct
    public void init() {
        properties.put("mail.smtp.host", mailSmtpHost);
        properties.put("mail.smtp.port", mailSmtpPort);
        properties.put("mail.smtp.starttls.enable", tlsEnable);
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("activation.url", url);
        properties.put("mail.activation.url", mailUrl);
        properties.put("reset.password.url", resetUrl);

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        });
    }

    public void sendLinkToActivateAccount(String to, String subject, String content) {
        sendEmail(to, subject, properties.getProperty("activation.url") + "/" + content);
    }

    public void sendLinkToConfirmAnEmail(String to, String content) {
        sendEmail(to, MAIL_SUBJECT_CONFIRMATION_LINK, properties.getProperty("mail.activation.url") + "/" + content);
    }

    public void sendInformationAdminLoggedIn(String to, String ipAddress, String language) {
        sendEmail(to, internationalization.getMessage("mail.account.information.title", language),
                internationalization.getMessage("mail.account.information.message.one", language) + ": " + ipAddress +
                        " " + internationalization.getMessage("mail.account.information.message.two", language));
    }

    public void sendInformationAboutChangedPasswordByAdmin(String to, String token, String language) {
        sendEmail(to, internationalization.getMessage("mail.account.password.changed.title", language),
                CHANGED_PASSWORD_BY_ADMIN_CONTENT_MESSAGE.formatted(properties.getProperty("reset.password.url"), token));
    }

    public void sendInformationAboutResettingPassword(String to, String token, String language) {
        sendEmail(to, internationalization.getMessage("mail.account.password.reset.title", language),
                RESET_PASSWORD_CONTENT_MESSAGE.formatted(properties.getProperty("reset.password.url"), token));
    }

    public void sendInformationAddingAnAccessLevel(String to, String role, String language) {
        sendEmail(to, internationalization.getMessage("mail.account.information.title", language),
                internationalization.getMessage("mail.account.information.message.start", language) + " " + role + " " + internationalization.getMessage("mail.account.information.message.end", language));
    }

    public void sendInformationRevokeAnAccessLevel(String to, String role, String language) {
        sendEmail(to, internationalization.getMessage("mail.account.information.title", language),
                internationalization.getMessage("mail.account.information.message.start", language) + " " + role +
                        " " + internationalization.getMessage("mail.account.information.message.end", language));
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

    public void sendReminderAboutAccountConfirmation(String to, String activationToken, String language) {
        sendEmail(to, internationalization.getMessage("mail.account.reminder.title", language),
                internationalization.getMessage("mail.account.reminder.message.one", language) + ", \n" +
                        internationalization.getMessage("mail.account.reminder.message.two", language) + " \n" +
                        internationalization.getMessage("mail.account.reminder.message.link", language) + ": " + properties.getProperty("activation.url") + activationToken);
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(login));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            throw AppException.createMailNotSentException();
        }
    }
}