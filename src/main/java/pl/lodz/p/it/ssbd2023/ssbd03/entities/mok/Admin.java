package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AccessLevelMapping;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "admin")
public class Admin extends AccessLevelMapping {



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
