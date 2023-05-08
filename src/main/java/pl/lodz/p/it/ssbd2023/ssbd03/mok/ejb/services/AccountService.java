package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangePhoneNumberDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;

@Local
public interface AccountService {
    void createOwner(Account account);

    void confirmAccountFromActivationLink(String confirmationToken);

    String authenticate(LoginDTO loginDTO);

    void changePhoneNumber(ChangePhoneNumberDTO changePhoneNumberDTO);

    PersonalData getUserPersonalData(String username);

    void editSelfPersonalData(String firstName, String surname);

    void editUserPersonalData(String username, String firstName, String surname);

    void changePassword(String oldPassword, String newPassword, String newReapetedPassowrd);
}
