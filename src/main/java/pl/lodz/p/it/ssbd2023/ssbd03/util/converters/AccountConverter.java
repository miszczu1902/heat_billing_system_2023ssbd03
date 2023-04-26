package pl.lodz.p.it.ssbd2023.ssbd03.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;

public class AccountConverter {
    public static PersonalData createOwnerDTOToOwner(CreateOwnerDTO createOwnerDTO) {
        Account account = new Account(
                createOwnerDTO.getEmail(),
                createOwnerDTO.getUsername(),
                createOwnerDTO.getPassword(),
                false,
                false,
                createOwnerDTO.getLanguage());
        return new PersonalData(account, createOwnerDTO.getFirstName(), createOwnerDTO.getSurname());
    }
}
