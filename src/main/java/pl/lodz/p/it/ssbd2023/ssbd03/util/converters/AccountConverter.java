package pl.lodz.p.it.ssbd2023.ssbd03.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;

public class AccountConverter {
    public static PersonalData createOwnerDTOToPersonalData(CreateOwnerDTO createOwnerDTO) {
        Account account = new Account(
                createOwnerDTO.getEmail(),
                createOwnerDTO.getUsername(),
                createOwnerDTO.getPassword(),
                true,
                false,
                createOwnerDTO.getLanguage());
        Owner owner = new Owner(createOwnerDTO.getPhoneNumber());
        owner.setAccount(account);
        account.getAccessLevels().add(owner);
        return new PersonalData(account, createOwnerDTO.getFirstName(), createOwnerDTO.getSurname());
    }
}
