package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import java.time.LocalDateTime;

public class Owner extends Account {

    public Owner(Long id, String email, String password, Boolean isEnable, LocalDateTime registerDate, String language, LoginData loginData, PersonalData personalData) {
        super(id, email, password, isEnable, registerDate, language, loginData, personalData);
    }
}