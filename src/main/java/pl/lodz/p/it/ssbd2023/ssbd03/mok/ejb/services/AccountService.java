package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangePhoneNumberDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Admin;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;

@Local
public interface AccountService {
    void createOwner(PersonalData personalData);

    String authenticate(LoginDTO loginDTO);

    void changePhoneNumber(ChangePhoneNumberDTO changePhoneNumberDTO);

    Owner getOwner();

    Manager getManager();

    Admin getAdmin();

    Owner getOwner(Long id);

    Manager getManager(Long id);

    Admin getAdmin(Long id);

    PersonalData getPersonalData(Owner owner);

    PersonalData getPersonalData(Manager manager);

    PersonalData getPersonalData(Admin admin);

    void changePassword(String oldPassword, String newPassword, String newReapetedPassowrd);
}
