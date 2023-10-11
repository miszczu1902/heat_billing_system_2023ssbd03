package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Admin;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.PersonalData;

import java.util.List;

@Local
public interface AccountService extends CommonManagerLocalInterface {
    void createOwner(Account account);

    void confirmAccountFromActivationLink(String confirmationToken);

    void changePasswordFromResetPasswordLink(String resetPasswordToken, String newPassword, String newRepeatedPassword);

    String authenticate(String username, String password);

    String refreshToken(String token);

    String updateLoginData(String username, boolean flag, String ipAddr);

    void changePhoneNumber(String changingUsername, String newPhoneNumber, String etag, Long version);

    void adminLoggedInEmail(String email, String ipAddr, String language);

    void changeSelfEmail(String username, String newEmail, String etag, Long version);

    void changeUserEmail(String changingUsername, String newEmail, String username, String etag, Long version);

    void confirmNewEmailAccountFromActivationLink(String confirmationToken);

    Account getAccount(String username);

    Account getSelfAccount(String username);

    Owner getOwner(String username);

    Manager getManager(String username);

    Admin getAdmin(String username);

    PersonalData getUserPersonalData(String username);

    PersonalData getSelfPersonalData(String username);

    void editSelfPersonalData(
            String username,
            String firstName,
            String surname,
            String etag,
            Long version
    );

    void editUserPersonalData(
            String editorUsername,
            String username,
            String firstName,
            String surname,
            String etag,
            Long version
    );

    void changeLanguage(String username, String language, String etag, Long version);

    void changeSelfPassword(
            String username,
            String oldPassword,
            String newPassword,
            String newRepeatedPassword,
            String etag,
            Long version
    );

    void changeUserPassword(
            String username,
            String newPassword,
            String newRepeatedPassword,
            String etag,
            Long version
    );

    void resetPassword(String username);

    void disableUserAccount(String editor, String username);

    void enableUserAccount(String editor, String username);

    void addAccessLevelManager(
            String adminUsername,
            String username,
            String license,
            String etag,
            Long version
    );

    void addAccessLevelOwner(
            String adminUsername,
            String username,
            String phoneNumber,
            String etag,
            Long version
    );

    void addAccessLevelAdmin(String adminUsername, String username, String etag, Long version);

    void revokeAccessLevel(
            String adminUsername,
            String username,
            String accessLevel,
            String etag,
            Long version
    );

    List<Account> getListOfAccounts(String sortBy, int pageNumber, int pageSize, Boolean isEnable);
}