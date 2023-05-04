package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Local;
import jakarta.persistence.NoResultException;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangePhoneNumberDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;


@Local
public interface AccountService {
    void createOwner(PersonalData personalData);

    String authenticate(LoginDTO loginDTO);

    void changePhoneNumber(ChangePhoneNumberDTO changePhoneNumberDTO);

    void editPersonalData(String firstName, String surname);

    void changePassword(String oldPassword, String newPassword, String newReapetedPassowrd);

    void blockUserAccount(String username) throws IllegalArgumentException, NoResultException;

    void unblockUserAccount(String username) throws IllegalArgumentException, NoResultException;
}
