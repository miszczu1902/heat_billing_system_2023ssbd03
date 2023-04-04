package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import java.time.LocalDateTime;

public class Admin extends Account {


    public Admin(Long id, String email, String password, Boolean isEnable, LocalDateTime registerDate, String language, LoginData loginData, PersonalData personalData) {
        super(id, email, password, isEnable, registerDate, language, loginData, personalData);
    }

    public void editUserAccount(int accountId, String name, String surname, String language) {

    }

    public void setUserAccountEnable(int accountId, Boolean enable) {

    }

    public void editUserEmail(int accountId, String email) {

    }

    public void editUserPassword(int accountId, String password) {

    }

    public void editUserRole(int accountId, String role) {

    }
}
