package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangePhoneNumberDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;

@Local
public interface AccountService {
    void createOwner(PersonalData personalData);

    String authenticate(LoginDTO loginDTO);

    void changePhoneNumber(ChangePhoneNumberDTO changePhoneNumberDTO);

    void editSelfPersonalData(String firstName, String surname);

    void editUserPersonalData(String username, String firstName, String surname);

    void changePassword(String oldPassword, String newPassword, String newReapetedPassowrd);
}
