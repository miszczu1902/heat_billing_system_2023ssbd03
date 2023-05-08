package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Local;
import jakarta.persistence.NoResultException;
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

    String authenticate(String username, String password);

    void changePhoneNumber(String newPhoneNumber);

    Owner getOwner();

    Manager getManager();

    Admin getAdmin();

    PersonalData getPersonalData();

    void editSelfPersonalData(String firstName, String surname);

    void editUserPersonalData(String username, String firstName, String surname);

    void changePassword(String oldPassword, String newPassword, String newRepeatedPassword);

    void disableUserAccount(String username) throws IllegalArgumentException, NoResultException;

    void enableUserAccount(String username) throws IllegalArgumentException, NoResultException;

    void addAccessLevelManager(String username, String license);

    void addAccessLevelOwner(String username, String phoneNumber);

    void addAccessLevelAdmin(String username);

    void revokeAccessLevel(String username, String accessLevel);

    List<Account> getListOfAccounts(String sortBy, int pageNumber);
}
