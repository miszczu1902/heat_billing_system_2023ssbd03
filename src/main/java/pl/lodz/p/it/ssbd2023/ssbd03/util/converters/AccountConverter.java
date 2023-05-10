package pl.lodz.p.it.ssbd2023.ssbd03.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.AdminDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ManagerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.OwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;

public class AccountConverter {
    public static OwnerDTO createOwnerDTOEntity(Owner owner, PersonalData personalData) {
        return new OwnerDTO(owner.getAccount().getEmail(), owner.getAccount().getUsername(), personalData.getFirstName(), personalData.getSurname(), owner.getAccount().getLanguage_(), owner.getPhoneNumber());
    }

    public static ManagerDTO createManagerDTOEntity(Manager manager, PersonalData personalData) {
        return new ManagerDTO(manager.getAccount().getEmail(), manager.getAccount().getUsername(), personalData.getFirstName(), personalData.getSurname(), manager.getAccount().getLanguage_(), manager.getLicense());
    }

    public static AdminDTO createAdminDTOEntity(Admin admin, PersonalData personalData) {
        return new AdminDTO(admin.getAccount().getEmail(), admin.getAccount().getUsername(), personalData.getFirstName(), personalData.getSurname(), admin.getAccount().getLanguage_());
    }
}