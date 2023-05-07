package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AccountForListDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.OwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;

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
}
