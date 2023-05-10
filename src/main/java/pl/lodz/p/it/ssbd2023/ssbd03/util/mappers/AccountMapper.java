package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AccountForListDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AdminDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ManagerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.OwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;

public class AccountMapper {
    public static Account createOwnerDTOToAccount(CreateOwnerDTO createOwnerDTO) {
        Account account = new Account(
                createOwnerDTO.getEmail(),
                createOwnerDTO.getUsername(),
                createOwnerDTO.getPassword(),
                true,
                false,
                createOwnerDTO.getLanguage());
        PersonalData personalData = new PersonalData(account, createOwnerDTO.getFirstName(), createOwnerDTO.getSurname());
        Owner owner = new Owner(createOwnerDTO.getPhoneNumber());
        owner.setAccount(account);
        account.getAccessLevels().add(owner);
        account.setPersonalData(personalData);
        return account;
    }

    public static AccountForListDTO accountToAccountForListDTO(Account account) {
        return new AccountForListDTO(
                account.getId(),
                account.getVersion(),
                account.getEmail(),
                account.getUsername());
    }

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
