package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "manager")
public class Manager extends Account {

    @Column(nullable = false)
    private String license;

    public Manager(Long id, String email, String password, Boolean isEnable, LocalDateTime registerDate, String language, LoginData loginData, PersonalData personalData, String license) {
        super(id, email, password, isEnable, registerDate, language, loginData, personalData);
        this.license=license;
    }


    public void editOwnerAccount(int ownerId, String name, String surname, String language) {

    }

    public void setOwnerAccountEnable(int ownerId, Boolean enable) {

    }

    public void editOwnerEmail(int ownerId, String Email) {

    }

    public void editOwnerRole(int accountId, String role) {

    }


}
