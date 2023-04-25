package pl.lodz.p.it.ssbd2023.ssbd03.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;

public class AccountConverter {
    public static Owner createOwnerDTOToOwner(CreateOwnerDTO createOwnerDTO) {
        return new Owner(createOwnerDTO.getPhoneNumber());
    }
}
