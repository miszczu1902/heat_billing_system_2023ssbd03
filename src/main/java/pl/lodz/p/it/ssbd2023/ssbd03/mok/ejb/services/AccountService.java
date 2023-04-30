package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;

@Local
public interface AccountService {
    void createOwner(PersonalData personalData);

    String authenticate(LoginDTO loginDTO);
}