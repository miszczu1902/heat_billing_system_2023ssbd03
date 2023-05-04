package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import pl.lodz.p.it.ssbd2023.ssbd03.auth.ConfirmationTokenGenerator;
import pl.lodz.p.it.ssbd2023.ssbd03.auth.JwtGenerator;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangePhoneNumberDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccessLevelMapping;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account.AccountPasswordException;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountConfirmationTokenFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.OwnerFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mail.MailSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({TrackerInterceptor.class})
public class AccountServiceImpl extends AbstractService implements AccountService, SessionSynchronization {
    @Inject
    private OwnerFacade ownerFacade;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private AccountConfirmationTokenFacade accountConfirmationTokenFacade;

    @Inject
    private MailSender mailSender;

    @Inject
    private ConfirmationTokenGenerator confirmationTokenGenerator;

    @Inject
    private JwtGenerator jwtGenerator;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private SecurityContext securityContext;

    @Override
    @RolesAllowed(Roles.GUEST)
    public void createOwner(Account account) {
        account.setPassword(BcryptHashGenerator.generateHash(account.getPassword()));
        account.setRegisterDate(LocalDateTime.now());

        accountFacade.create(account);

        AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken(
                confirmationTokenGenerator.createAccountConfirmationToken(), account);
        accountConfirmationTokenFacade.create(accountConfirmationToken);

        mailSender.sendLinkToActivateAccountToEmail(account.getEmail(), "Activate account", accountConfirmationToken.getTokenValue());
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public void confirmAccountFromActivationLink(String confirmationToken) {
        AccountConfirmationToken accountConfirmationToken = accountConfirmationTokenFacade.getActivationTokenByTokenValue(confirmationToken);

        Account accountToActivate = accountConfirmationToken.getAccount();
        accountToActivate.setIsActive(true);
        accountFacade.edit(accountToActivate);

        accountConfirmationTokenFacade.remove(accountConfirmationToken);
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public String authenticate(LoginDTO loginDTO) {
        Account account = accountFacade.findByUsername(loginDTO.getUsername());

        if (BcryptHashGenerator.generateHash(loginDTO.getPassword()).equals(account.getPassword())) {
//            UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredential(loginDTO.getUsername(), new Password(loginDTO.getPassword()));
//            CredentialValidationResult credentialValidationResult = identityStoreHandler.validate(usernamePasswordCredential);

            List<String> roles = account.getAccessLevels().stream()
                    .map(AccessLevelMapping::getAccessLevel)
                    .collect(Collectors.toList());

            return jwtGenerator.generateJWT(loginDTO.getUsername(), roles);
        }
        throw new IllegalArgumentException();
    }

    @Override
    @RolesAllowed(Roles.OWNER)
    public void changePhoneNumber(ChangePhoneNumberDTO changePhoneNumberDTO) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByUsername(username);
        Owner owner = (Owner) account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Owner)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Account is not an Owner."));
        final String newPhoneNumber = changePhoneNumberDTO.getPhoneNumber();
        if (!newPhoneNumber.equals(owner.getPhoneNumber())) {
            if (accountFacade.findByPhoneNumber(newPhoneNumber) == null) {
                owner.setPhoneNumber(newPhoneNumber);
                ownerFacade.edit(owner);
            } else {
                throw new IllegalArgumentException("Phone number is already taken.");
            }
        } else {
            throw new IllegalArgumentException("The given number is taken by your account.");
        }
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.OWNER})
    public void changePassword(String oldPassword, String newPassword, String newRepeatedPassword) throws AccountPasswordException {
        if (oldPassword.equals(newPassword)) {
            throw new AccountPasswordException("Old password and new password are the same.");
        }
        if (!newPassword.equals(newRepeatedPassword)) {
            throw new AccountPasswordException("New password and new repeated password are not the same.");
        }
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByUsername(username);
        if (!BcryptHashGenerator.generateHash(oldPassword).equals(account.getPassword())) {
            throw new AccountPasswordException("Old password is incorrect");
        }
        final String newPasswordHash = BcryptHashGenerator.generateHash(newPassword);
        account.setPassword(newPasswordHash);
        accountFacade.edit(account);
    }

}
