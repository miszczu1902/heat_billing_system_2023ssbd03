package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.*;
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
                account.getUsername(),
                account.getIsEnable(),
                account.getIsActive());
    }

    public static AccountInfoDTO createAccountInfoDTOEntity(Account account) {
        final String phoneNumber = account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Owner)
                .map(accessLevel -> (Owner) accessLevel)
                .findAny()
                .map(Owner::getPhoneNumber)
                .orElse(null);

        final String license = account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Manager)
                .map(accessLevel -> (Manager) accessLevel)
                .findAny()
                .map(Manager::getLicense)
                .orElse(null);

        return new AccountInfoDTO(
                account.getId(),
                account.getVersion(),
                account.getEmail(),
                account.getUsername(),
                account.getIsEnable(),
                account.getIsActive(),
                account.getRegisterDate().toString(),
                account.getAccessLevels().stream()
                        .filter(accessLevel -> accessLevel instanceof Owner)
                        .map(accessLevel -> (Owner) accessLevel)
                        .findAny()
                        .map(Owner::getIsActive)
                        .orElse(null),
                account.getAccessLevels().stream()
                        .filter(accessLevel -> accessLevel instanceof Manager)
                        .map(accessLevel -> (Manager) accessLevel)
                        .findAny()
                        .map(Manager::getIsActive)
                        .orElse(null),
                account.getAccessLevels().stream()
                        .filter(accessLevel -> accessLevel instanceof Admin)
                        .map(accessLevel -> (Admin) accessLevel)
                        .findAny()
                        .map(Admin::getIsActive)
                        .orElse(null),
                account.getPersonalData().getFirstName(),
                account.getPersonalData().getSurname(),
                phoneNumber,
                license);
    }

    public static OwnerDTO createOwnerDTOEntity(Owner owner, PersonalData personalData) {
        return new OwnerDTO(owner.getAccount().getId(), owner.getAccount().getVersion(),
                owner.getAccount().getEmail(),
                owner.getAccount().getUsername(),
                personalData.getFirstName(),
                personalData.getSurname(),
                owner.getAccount().getLanguage_(),
                owner.getPhoneNumber());
    }

    public static ManagerDTO createManagerDTOEntity(Manager manager, PersonalData personalData) {
        return new ManagerDTO(manager.getAccount().getId(), manager.getAccount().getVersion(),
                manager.getAccount().getEmail(),
                manager.getAccount().getUsername(),
                personalData.getFirstName(),
                personalData.getSurname(),
                manager.getAccount().getLanguage_(),
                manager.getLicense());
    }

    public static AdminDTO createAdminDTOEntity(Admin admin, PersonalData personalData) {
        return new AdminDTO(admin.getAccount().getId(), admin.getAccount().getVersion(),
                admin.getAccount().getEmail(),
                admin.getAccount().getUsername(),
                personalData.getFirstName(),
                personalData.getSurname(),
                admin.getAccount().getLanguage_());
    }
}
