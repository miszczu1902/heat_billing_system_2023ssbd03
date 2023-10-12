package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import pl.lodz.p.it.ssbd2023.ssbd03.auth.JwtGenerator;
import pl.lodz.p.it.ssbd2023.ssbd03.auth.TokenGenerator;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.*;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.*;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.mail.MailSender;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Internationalization;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@ApplicationScoped
@Transactional(value = Transactional.TxType.REQUIRES_NEW, rollbackOn = AppException.class)
public class AccountServiceImpl extends AbstractService implements AccountService {
    @Inject
    PersonalDataFacade personalDataFacade;

    @Inject
    Internationalization internationalization;

    @Inject
    OwnerFacade ownerFacade;

    @Inject
    AccountFacade accountFacade;

    @Inject
    ManagerFacade managerFacade;

    @Inject
    AdminFacade adminFacade;

    @Inject
    AccountConfirmationTokenFacade accountConfirmationTokenFacade;

    @Inject
    EmailConfirmationTokenFacade emailConfirmationTokenFacade;

    @Inject
    ResetPasswordTokenFacade resetPasswordTokenFacade;

    @Inject
    MailSender mailSender;

    @Inject
    TokenGenerator tokenGenerator;

    @Inject
    JwtGenerator jwtGenerator;

    @Inject
    IdentityStore identityStoreHandler;

    @Inject
    BcryptHashGenerator bcryptHashGenerator;

    @Inject
    LoginDataFacade loginDataFacade;

    @Inject
    MessageSigner messageSigner;


    @Override
    @RolesAllowed(Roles.GUEST)
    public void createOwner(Account account) {
        account.setPassword(bcryptHashGenerator.generate(account.getPassword().toCharArray()));
        account.setRegisterDate(LocalDateTime.now(TIME_ZONE));

        accountFacade.create(account);

        AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken(
                tokenGenerator.createAccountConfirmationToken(), account);
        accountConfirmationTokenFacade.create(accountConfirmationToken);

        mailSender.sendLinkToActivateAccount(account.getEmail(), internationalization.getMessage("mail.account.activate.title", account.getLanguage_()), accountConfirmationToken.getTokenValue());
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public void confirmAccountFromActivationLink(String confirmationToken) {
        List<AccountConfirmationToken> accountConfirmationToken = accountConfirmationTokenFacade.getActivationTokenByTokenValue(confirmationToken);

        if (accountConfirmationToken.isEmpty()) {
            throw AppException.tokenNoResultException();
        }
        Account accountToActivate = accountConfirmationToken.get(0).getAccount();
        accountToActivate.setIsActive(true);
        accountFacade.edit(accountToActivate);
        accountConfirmationTokenFacade.remove(accountConfirmationToken.get(0));
        mailSender.sendInformationAccountActivated(accountToActivate.getEmail(), accountConfirmationToken.get(0).getAccount().getLanguage_());
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public String updateLoginData(String username, boolean flag, String ipAddr) {
        try {
            final Account account = accountFacade.findByUsername(username);
            final LoginData loginData = loginDataFacade.findById(account);
            if (flag) {
                if (account.getAccessLevels().stream().anyMatch(accessLevelMapping -> accessLevelMapping.getAccessLevel().equals(Roles.ADMIN))) {
                    adminLoggedInEmail(account.getEmail(), ipAddr, account.getLanguage_());
                }
                loginData.setInvalidLoginCounter(0);
                loginData.setLastValidLogicAddress(ipAddr);
                loginData.setLastValidLoginDate(LocalDateTime.now(ZoneId.of(LoadConfig.loadPropertyFromConfig("zone"))));
            } else {
                loginData.setInvalidLoginCounter(loginData.getInvalidLoginCounter() + 1);
                loginData.setLastInvalidLogicAddress(ipAddr);
                loginData.setLastInvalidLoginDate(LocalDateTime.now(ZoneId.of(LoadConfig.loadPropertyFromConfig("zone"))));
                if (loginData.getInvalidLoginCounter() == 3) {
                    account.setIsEnable(false);
                    mailSender.sendInformationAccountDisabled(account.getEmail(), account.getLanguage_());
                }
            }
            loginDataFacade.edit(loginData);
            return account.getLanguage_();
        } catch (Exception ex) {
            throw AppException.invalidCredentialsException();
        }
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public void adminLoggedInEmail(String email, String ipAddr, String language) {
        mailSender.sendInformationAdminLoggedIn(email, ipAddr, language);
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public String authenticate(String username, String password) {
        final UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredential(username, new Password(password));
        final CredentialValidationResult credentialValidationResult = identityStoreHandler.validate(usernamePasswordCredential);
        if (credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
            final Set<String> roles = credentialValidationResult.getCallerGroups();
            return jwtGenerator.generateJwtToken(username, roles);
        }
        throw AppException.invalidCredentialsException();
    }

    @Override
    @RolesAllowed({Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public String refreshToken(String token) {
        return jwtGenerator.refreshTokenJWT();
    }

    @Override
    @RolesAllowed({Roles.GUEST})
    public void resetPassword(String username) {
        final Account accountToChangePassword = accountFacade.findByUsername(username);
        if (!accountToChangePassword.getIsActive()) {
            throw AppException.createAccountIsNotActivatedException();
        }
        if (!accountToChangePassword.getIsEnable()) {
            throw AppException.createAccountIsBlockedException();
        }

        String token = tokenGenerator.createResetPasswordToken();
        while (resetPasswordTokenFacade.checkIfResetPasswordTokenExistsByTokenValue(token)) {
            token = tokenGenerator.createResetPasswordToken();
        }
        final ResetPasswordToken resetPasswordToken = new ResetPasswordToken(
                token, accountToChangePassword);
        resetPasswordTokenFacade.create(resetPasswordToken);

        mailSender.sendInformationAboutResettingPassword(accountToChangePassword.getEmail(), token, accountToChangePassword.getLanguage_());
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public void changePasswordFromResetPasswordLink(String token, String newPassword, String newRepeatedPassword) {

        if (!newPassword.equals(newRepeatedPassword)) {
            throw AppException.createPasswordsNotSameException();
        }
        final List<ResetPasswordToken> resetPasswordToken = resetPasswordTokenFacade.getResetPasswordByTokenValue(token);

        if (resetPasswordToken.isEmpty()) {
            throw AppException.tokenNoResultException();
        }

        final Account accountToChangePassword = resetPasswordToken.get(0).getAccount();
        changePassword(accountToChangePassword, newPassword);

        resetPasswordTokenFacade.remove(resetPasswordToken.get(0));
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.OWNER})
    public void changeSelfPassword(
            String username,
            String oldPassword,
            String newPassword,
            String newRepeatedPassword,
            String etag,
            Long version
    ) {
        if (oldPassword.equals(newPassword)) {
            throw AppException.createSameOldAndNewPasswordException();
        }
        if (!newPassword.equals(newRepeatedPassword)) {
            throw AppException.createPasswordsNotSameException();
        }

        final Account account = accountFacade.findByUsername(username);

        if (!etag.equals(messageSigner.sign(account))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, account.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        if (!bcryptHashGenerator.verify(oldPassword.toCharArray(), account.getPassword())) {
            throw AppException.createPasswordOldIncorrectException();
        }
        final String newPasswordHash = bcryptHashGenerator.generate(newPassword.toCharArray());
        account.setPassword(newPasswordHash);
        accountFacade.edit(account);
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.OWNER})
    public PersonalData getUserPersonalData(String username) {
        final Account account = accountFacade.findByUsername(username);
        return personalDataFacade.find(account.getId());
    }

    @RolesAllowed({Roles.ADMIN})
    public void changeUserPassword(String username, String newPassword, String newRepeatedPassword, String etag, Long version) {
        if (!newPassword.equals(newRepeatedPassword)) {
            throw AppException.createPasswordsNotSameException();
        }
        final Account accountToChangePassword = accountFacade.findByUsername(username);

        if (!etag.equals(messageSigner.sign(accountToChangePassword))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, accountToChangePassword.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        changePassword(accountToChangePassword, newPassword);

        String token = tokenGenerator.createResetPasswordToken();
        while (resetPasswordTokenFacade.checkIfResetPasswordTokenExistsByTokenValue(token)) {
            token = tokenGenerator.createResetPasswordToken();
        }
        final ResetPasswordToken resetPasswordToken = new ResetPasswordToken(
                token, accountToChangePassword);
        resetPasswordTokenFacade.create(resetPasswordToken);

        mailSender.sendInformationAboutChangedPasswordByAdmin(accountToChangePassword.getEmail(), token, accountToChangePassword.getLanguage_());
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Account getAccount(String username) {
        return accountFacade.findByUsername(username);
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.OWNER})
    public Account getSelfAccount(final String username) {
        ;
        return accountFacade.findByUsername(username);
    }

    @Override
    @RolesAllowed(Roles.OWNER)
    public Owner getOwner(final String username) {
        final Account account = accountFacade.findByUsername(username);
        return (Owner) account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Owner)
                .map(accessLevel -> (Owner) accessLevel)
                .findAny()
                .orElseThrow(AppException::createAccountIsNotOwnerException);
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public Manager getManager(final String username) {
        final Account account = accountFacade.findByUsername(username);
        return (Manager) account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Manager)
                .map(accessLevel -> (Manager) accessLevel)
                .findAny()
                .orElseThrow(AppException::createAccountIsNotManagerException);
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public Admin getAdmin(final String username) {
        final Account account = accountFacade.findByUsername(username);
        return (Admin) account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Admin)
                .map(accessLevel -> (Admin) accessLevel)
                .findAny()
                .orElseThrow(AppException::createAccountIsNotAdminException);
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.OWNER})
    public PersonalData getSelfPersonalData(final String username) {
        final Account account = accountFacade.findByUsername(username);
        return personalDataFacade.find(account.getId());
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public void editSelfPersonalData(
            String username,
            String firstName,
            String surname,
            String etag,
            Long version
    ) {
        editPersonalData(username, firstName, surname, etag, version);
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public void changeLanguage(String username, String language, String etag, Long version) {
        final Account account = accountFacade.findByUsername(username);
        if (!etag.equals(messageSigner.sign(account))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, account.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }
        account.setLanguage_(language);
        accountFacade.edit(account);
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public void editUserPersonalData(
            String editorUsername,
            String username,
            String firstName,
            String surname,
            String etag,
            Long version
    ) {
        final Account editorAccount = accountFacade.findByUsername(editorUsername);
        final Account editableAccount = accountFacade.findByUsername(username);

        if (editorAccount.getAccessLevels().stream().anyMatch(accessLevelMapping -> accessLevelMapping.getAccessLevel().equals(Roles.ADMIN))) {
            editPersonalData(username, firstName, surname, etag, version);
        } else if (editableAccount.getAccessLevels().stream().noneMatch(accessLevelMapping -> accessLevelMapping.getAccessLevel().equals(Roles.ADMIN))) {
            editPersonalData(username, firstName, surname, etag, version);
        } else {
            throw AppException.createNotAllowedActionException();
        }
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public List<Account> getListOfAccounts(String sortBy, int pageNumber, int pageSize, Boolean isEnable) {
        return accountFacade.getListOfAccountsWithFilterParams(sortBy, pageNumber, pageSize, isEnable);
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public void disableUserAccount(String editor, String username) {
        editUserEnableFlag(editor, username, false);
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public void enableUserAccount(String editor, String username) {
        editUserEnableFlag(editor, username, true);
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void addAccessLevelManager(
            String adminUsername,
            String username,
            String license,
            String etag,
            Long version
    ) {
        if (username.equals(adminUsername)) {
            throw AppException.addingAnAccessLevelToTheSameAdminAccount();
        }
        final Account account = accountFacade.findByUsername(username);
        if (!etag.equals(messageSigner.sign(account))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, account.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        if (!account.getIsActive()) {
            throw AppException.createAccountIsNotActivatedException();
        }
        if (managerFacade.findByLicense(license, username)) {
            throw AppException.createAccountWithLicenseExistsException();
        }
        final Optional<Manager> manager = account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Manager)
                .map(accessLevel -> (Manager) accessLevel).findAny();

        manager.ifPresentOrElse(managerAccess -> {
                    if (managerAccess.getIsActive()) {
                        throw AppException.theAccessLevelisAlreadyGranted();
                    }
                    if (managerAccess.getLicense().equals(license)) {
                        managerAccess.setIsActive(true);
                        managerFacade.edit(managerAccess);
                    } else {
                        managerAccess.setIsActive(true);
                        managerAccess.setLicense(license);
                        managerFacade.edit(managerAccess);
                    }
                    mailSender.sendInformationAddingAnAccessLevel(account.getEmail(), "manager", account.getLanguage_());
                }, () -> {
                    final Manager newManager = new Manager(license);
                    newManager.setAccount(account);
                    managerFacade.create(newManager);
                    account.getAccessLevels().add(newManager);
                    accountFacade.edit(account);
                    mailSender.sendInformationAddingAnAccessLevel(account.getEmail(), "manager", account.getLanguage_());
                }
        );
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void addAccessLevelOwner(
            String adminUsername,
            String username,
            String phoneNumber,
            String etag,
            Long version
    ) {
        if (username.equals(adminUsername)) {
            throw AppException.addingAnAccessLevelToTheSameAdminAccount();
        }
        final Account account = accountFacade.findByUsername(username);
        if (!etag.equals(messageSigner.sign(account))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, account.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        if (!account.getIsActive()) {
            throw AppException.createAccountIsNotActivatedException();
        }
        if (ownerFacade.checkIfAnOwnerExistsByPhoneNumberAndWithoutUsername(phoneNumber, username)) {
            throw AppException.createAccountWithNumberExistsException();
        }
        final Optional<Owner> owner = account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Owner)
                .map(accessLevel -> (Owner) accessLevel).findAny();

        owner.ifPresentOrElse(ownerAccess -> {
                    if (ownerAccess.getIsActive()) {
                        throw AppException.theAccessLevelisAlreadyGranted();
                    }
                    if (ownerAccess.getPhoneNumber().equals(phoneNumber)) {
                        ownerAccess.setIsActive(true);
                        ownerFacade.edit(ownerAccess);
                    } else {
                        ownerAccess.setIsActive(true);
                        ownerAccess.setPhoneNumber(phoneNumber);
                        ownerFacade.edit(ownerAccess);
                    }
                    mailSender.sendInformationAddingAnAccessLevel(account.getEmail(), "owner", account.getLanguage_());
                }, () -> {
                    final Owner newOwner = new Owner(phoneNumber);
                    newOwner.setAccount(account);
                    ownerFacade.create(newOwner);
                    account.getAccessLevels().add(newOwner);
                    accountFacade.edit(account);
                    mailSender.sendInformationAddingAnAccessLevel(account.getEmail(), "owner", account.getLanguage_());
                }
        );
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void addAccessLevelAdmin(String adminUsername, String username, String etag, Long version) {
        if (username.equals(adminUsername)) {
            throw AppException.addingAnAccessLevelToTheSameAdminAccount();
        }
        final Account account = accountFacade.findByUsername(username);
        if (!etag.equals(messageSigner.sign(account))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, account.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        if (!account.getIsActive()) {
            throw AppException.createAccountIsNotActivatedException();
        }
        final Optional<Admin> admin = account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Admin)
                .map(accessLevel -> (Admin) accessLevel).findAny();

        admin.ifPresentOrElse(adminAccess -> {
                    if (adminAccess.getIsActive()) {
                        throw AppException.theAccessLevelisAlreadyGranted();
                    } else {
                        adminAccess.setIsActive(true);
                        adminFacade.edit(adminAccess);
                        mailSender.sendInformationAddingAnAccessLevel(account.getEmail(), "admin", account.getLanguage_());
                    }
                }, () -> {
                    final Admin newAdmin = new Admin();
                    newAdmin.setAccount(account);
                    adminFacade.create(newAdmin);
                    account.getAccessLevels().add(newAdmin);
                    accountFacade.edit(account);
                    mailSender.sendInformationAddingAnAccessLevel(account.getEmail(), "admin", account.getLanguage_());
                }
        );
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void revokeAccessLevel(
            String adminUsername,
            String username,
            String access,
            String etag,
            Long version
    ) {
        if (username.equals(adminUsername)) {
            throw AppException.revokeAnAccessLevelToTheSameAdminAccount();
        }
        final Account account = accountFacade.findByUsername(username);
        if (!etag.equals(messageSigner.sign(account))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, account.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        if (!account.getIsActive()) {
            throw AppException.createAccountIsNotActivatedException();
        }
        final int size = (int) account.getAccessLevels().stream()
                .filter(AccessLevelMapping::getIsActive)
                .count();
        if (size <= 1) {
            throw AppException.revokeTheOnlyLevelOfAccess();
        }
        if (access.equals(Roles.MANAGER)) {
            final Manager manager = account.getAccessLevels().stream()
                    .filter(accessLevel -> accessLevel instanceof Manager && accessLevel.getIsActive())
                    .map(accessLevel -> (Manager) accessLevel)
                    .findAny()
                    .orElseThrow(AppException::createAccountIsNotManagerException);
            manager.setIsActive(false);
            managerFacade.edit(manager);
            mailSender.sendInformationRevokeAnAccessLevel(account.getEmail(), "manager", account.getLanguage_());
        }
        if (access.equals(Roles.ADMIN)) {
            final Admin admin = account.getAccessLevels().stream()
                    .filter(accessLevel -> accessLevel instanceof Admin && accessLevel.getIsActive())
                    .map(accessLevel -> (Admin) accessLevel)
                    .findAny()
                    .orElseThrow(AppException::createAccountIsNotAdminException);
            admin.setIsActive(false);
            adminFacade.edit(admin);
            mailSender.sendInformationRevokeAnAccessLevel(account.getEmail(), "admin", account.getLanguage_());
        }
        if (access.equals(Roles.OWNER)) {
            final Owner owner = account.getAccessLevels().stream()
                    .filter(accessLevel -> accessLevel instanceof Owner && accessLevel.getIsActive())
                    .map(accessLevel -> (Owner) accessLevel)
                    .findAny()
                    .orElseThrow(AppException::createAccountIsNotOwnerException);
            owner.setIsActive(false);
            ownerFacade.edit(owner);
            mailSender.sendInformationRevokeAnAccessLevel(account.getEmail(), "owner", account.getLanguage_());
        }
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public void changeSelfEmail(String username, String newEmail, String etag, Long version) {
        final Account account = accountFacade.findByUsername(username);

        if (!etag.equals(messageSigner.sign(account))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, account.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }
        changeEmail(newEmail, account);
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public void changeUserEmail(String changingUsername, String newEmail, String username, String etag, Long version) {
        final Account changingAccount = accountFacade.findByUsername(changingUsername);
        final Account changedAccount = accountFacade.findByUsername(username);
        final Boolean changedAccountIsManager = changedAccount.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Admin)
                .map(accessLevel -> (Admin) accessLevel)
                .findAny().isPresent();
        final Boolean changingAccountIsAdmin = changingAccount.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Admin)
                .map(accessLevel -> (Admin) accessLevel)
                .findAny().isPresent();
        if (changedAccountIsManager && !changingAccountIsAdmin) {
            throw AppException.createManagerCanNotChangeAdminException();
        }

        if (!etag.equals(messageSigner.sign(changedAccount))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, changedAccount.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        changeEmail(newEmail, changedAccount);
    }

    @Override
    @RolesAllowed({Roles.GUEST, Roles.OWNER, Roles.ADMIN, Roles.MANAGER})
    public void confirmNewEmailAccountFromActivationLink(String confirmationToken) {
        final List<EmailConfirmationToken> emailConfirmationToken = emailConfirmationTokenFacade.getActivationTokenByTokenValue(confirmationToken);

        if (emailConfirmationToken.isEmpty()) {
            throw AppException.tokenNoResultException();
        }
        Account account = emailConfirmationToken.get(0).getAccount();
        final String newEmail = emailConfirmationToken.get(0).getEmail();
        account.setEmail(newEmail);
        accountFacade.edit(account);
        emailConfirmationTokenFacade.remove(emailConfirmationToken.get(0));
    }

    @Override
    @RolesAllowed(Roles.OWNER)
    public void changePhoneNumber(String changingUsername, String newPhoneNumber, String etag, Long version) {
        final Account account = accountFacade.findByUsername(changingUsername);
        Owner owner = account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Owner && accessLevel.getIsActive())
                .map(accessLevel -> (Owner) accessLevel)
                .findAny()
                .orElseThrow(AppException::createAccountIsNotOwnerException);

        if (!etag.equals(messageSigner.sign(owner))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, account.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        if (newPhoneNumber.equals(owner.getPhoneNumber())) {
            throw AppException.createCurrentPhoneNumberException();
        }
        if (ownerFacade.checkIfAnOwnerExistsByPhoneNumber(newPhoneNumber)) {
            throw AppException.createAccountWithNumberExistsException();
        }
        owner.setPhoneNumber(newPhoneNumber);
        ownerFacade.edit(owner);
    }

    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    void setUserEnableFlag(String username, boolean flag) {
        final Account editableAccount = accountFacade.findByUsername(username);
        editableAccount.setIsEnable(flag);
        accountFacade.edit(editableAccount);
    }

    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    void editUserEnableFlag(String editor, String username, boolean flag) {
        final Account editorAccount = accountFacade.findByUsername(editor);
        final Account editableAccount = accountFacade.findByUsername(username);

        if (editorAccount.equals(editableAccount)) {
            throw AppException.createNotAllowedActionException();
        }

        if ((editorAccount.getAccessLevels().stream()
                .anyMatch(accessLevelMapping -> accessLevelMapping.getAccessLevel().equals(Roles.ADMIN)))
                || (editableAccount.getAccessLevels()
                .stream().noneMatch(accessLevelMapping -> accessLevelMapping.getAccessLevel().equals(Roles.ADMIN)))) {
            setUserEnableFlag(username, flag);
        } else {
            throw AppException.createNotAllowedActionException();
        }
        if (flag) {
            mailSender.sendInformationAccountEnabled(editableAccount.getEmail(), editableAccount.getLanguage_());
        } else {
            mailSender.sendInformationAccountDisabled(editableAccount.getEmail(), editableAccount.getLanguage_());
        }
    }

    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    void editPersonalData(String username, String firstName, String surname, String etag, Long version) {
        PersonalData personalData = personalDataFacade.findByUsername(username);

        if (!etag.equals(messageSigner.sign(personalData))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, personalData.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        personalData.setFirstName(firstName);
        personalData.setSurname(surname);
        personalDataFacade.edit(personalData);
    }

    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    void changeEmail(String newEmail, Account account) {
        if (newEmail.equals(account.getEmail())) {
            throw AppException.createCurrentEmailException();
        }
        if (accountFacade.checkIfAnAccountExistsByEmail(newEmail)) {
            throw AppException.createAccountWithEmailExistsException();
        }
        final EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(
                tokenGenerator.createAccountConfirmationToken(), newEmail, account);
        emailConfirmationTokenFacade.create(emailConfirmationToken);
        mailSender.sendLinkToConfirmAnEmail(newEmail, emailConfirmationToken.getTokenValue());
    }

    @RolesAllowed({Roles.GUEST, Roles.ADMIN})
    void changePassword(Account account, String newPassword) {
        if (!account.getIsActive()) {
            throw AppException.createAccountIsNotActivatedException();
        }
        if (!account.getIsEnable()) {
            throw AppException.createAccountIsBlockedException();
        }
        final char[] newPasswordCharArray = newPassword.toCharArray();
        if (bcryptHashGenerator.verify(newPasswordCharArray, account.getPassword())) {
            throw AppException.createSameOldAndNewPasswordException();
        }
        final String newPasswordHash = bcryptHashGenerator.generate(newPasswordCharArray);
        account.setPassword(newPasswordHash);
        accountFacade.edit(account);
    }
}