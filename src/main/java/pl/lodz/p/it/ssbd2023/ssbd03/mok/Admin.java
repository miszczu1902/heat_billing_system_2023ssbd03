package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "admin")
public class Admin extends Account {

    public Admin(Long id, String email, String username, String password, Boolean isEnable, LocalDateTime registerDate, String language_, LoginData loginData, PersonalData personalData) {
        super(id, email, username, password, isEnable, registerDate, language_, loginData, personalData);
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
