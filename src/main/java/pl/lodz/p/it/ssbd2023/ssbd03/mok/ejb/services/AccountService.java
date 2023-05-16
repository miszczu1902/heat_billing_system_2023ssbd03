package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Admin;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;
import java.util.List;

@Local
public interface AccountService {
    void createOwner(Account account);

    void confirmAccountFromActivationLink(String confirmationToken);

    void changePasswordFromResetPasswordLink(String resetPasswordToken, String newPassword, String newRepeatedPassword);

    String authenticate(String username, String password);

    void updateLoginData(String username, boolean flag);

    void changePhoneNumber(String newPhoneNumber, String etag, Long version);

    void adminLoggedInEmail(String email);

    void changeSelfEmail(String newEmail, String etag, Long version);

    void changeUserEmail(String newEmail, String username, String etag, Long version);

    void confirmNewEmailAccountFromActivationLink(String confirmationToken);

    Account getAccount(String username);

    Owner getOwner();

    Manager getManager();

    Admin getAdmin();

    PersonalData getPersonalData(String username);

    PersonalData getSelfPersonalData();

    void editSelfPersonalData(String firstName, String surname, String etag, Long version);

    void editUserPersonalData(String username, String firstName, String surname, String etag, Long version);

    void changeSelfPassword(String oldPassword, String newPassword, String newRepeatedPassword, String etag, Long version);

    void changeUserPassword(String username, String newPassword, String newRepeatedPassword, String etag, Long version);

    void resetPassword(String username);

    void disableUserAccount(String username, String etag, Long version);

    void enableUserAccount(String username, String etag, Long version);

    void addAccessLevelManager(String username, String license);

    void addAccessLevelOwner(String username, String phoneNumber);

    void addAccessLevelAdmin(String username);

    void revokeAccessLevel(String username, String accessLevel);

    List<Account> getListOfAccounts(String sortBy, int pageNumber, int pageSize, Boolean isEnable);
}