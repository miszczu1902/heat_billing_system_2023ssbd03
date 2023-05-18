package pl.lodz.p.it.ssbd2023.ssbd03.system;

import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.EmailConfirmationToken;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountConfirmationTokenFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.EmailConfirmationTokenFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.ResetPasswordTokenFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.mail.MailSender;

import java.util.List;

@Startup
@Singleton
@RunAs(Roles.ADMIN)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(TrackerInterceptor.class)
public class SystemScheduler {
    @Inject
    private AccountConfirmationTokenFacade accountConfirmationTokenFacade;

    @Inject
    private EmailConfirmationTokenFacade emailConfirmationTokenFacade;

    @Inject
    private ResetPasswordTokenFacade resetPasswordTokenFacade;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private MailSender mailSender;

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void cleanUnconfirmedAccounts() {
        final List<AccountConfirmationToken> allUnconfirmedAccounts = accountConfirmationTokenFacade.findAllUnconfirmedAccounts();
        if (!allUnconfirmedAccounts.isEmpty()) {
            allUnconfirmedAccounts.forEach(accountConfirmationToken -> {
                accountFacade.remove(accountConfirmationToken.getAccount());
                accountConfirmationTokenFacade.remove(accountConfirmationToken);
            });
        }
    }

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void sendActivationReminder() {
        final List<AccountConfirmationToken> allUnconfirmedAccounts = accountConfirmationTokenFacade.findAllUnconfirmedAccountsToRemind();
        if (!allUnconfirmedAccounts.isEmpty()) {
            allUnconfirmedAccounts.forEach(accountConfirmationToken -> {
                if (!accountConfirmationToken.getIsReminderSent()) {
                    accountConfirmationToken.setIsReminderSent(true);
                    mailSender.sendReminderAboutAccountConfirmation(
                            accountConfirmationToken.getAccount().getEmail(), accountConfirmationToken.getTokenValue());
                }
            });
        }
    }

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void deleteNewEmailExpiredTokens() {
        final List<EmailConfirmationToken> emailConfirmationTokenList = emailConfirmationTokenFacade.getExpiredNewEmailTokensList();
        if (!emailConfirmationTokenList.isEmpty()) {
            emailConfirmationTokenList.forEach(emailConfirmationToken -> emailConfirmationTokenFacade.remove(emailConfirmationToken));
        }
    }

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void deleteResetPasswordExpiredTokens() {
        final List<ResetPasswordToken> resetPasswordTokens = resetPasswordTokenFacade.getExpiredResetPasswordTokensList();
        if (!resetPasswordTokens.isEmpty()) {
            resetPasswordTokens.forEach(resetPasswordToken -> resetPasswordTokenFacade.remove(resetPasswordToken));
        }
    }

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    public void unlockAccounts() {
        final List<Account> accounts = accountFacade.findAllBlockedAccounts();
        if (!accounts.isEmpty()) {
            accounts.forEach(account -> {
                account.setIsEnable(true);
                account.getLoginData().setInvalidLoginCounter(0);
                accountFacade.edit(account);
                mailSender.sendInformationAccountEnabled(account.getEmail(),account.getLanguage_());
            });
        }
    }
}
