package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Local;
import jakarta.persistence.NoResultException;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;

import java.util.List;

@Local
public interface AccountService {
    void createOwner(Account account);

    void confirmAccountFromActivationLink(String confirmationToken);

    String authenticate(String username, String password);

    void changePhoneNumber(String newPhoneNumber);

    void editSelfPersonalData(String firstName, String surname);

    void editUserPersonalData(String username, String firstName, String surname);

    void changePassword(String oldPassword, String newPassword, String newRepeatedPassword);

    void disableUserAccount(String username) throws IllegalArgumentException, NoResultException;

    void enableUserAccount(String username) throws IllegalArgumentException, NoResultException;

    List<Account> getListOfAccounts(String sortBy, int pageNumber);
}
