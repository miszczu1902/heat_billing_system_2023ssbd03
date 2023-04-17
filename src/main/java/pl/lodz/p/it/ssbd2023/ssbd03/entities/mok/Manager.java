package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AccessLevelMapping;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("MANAGER")
@Table(name = "manager")
public class Manager extends AccessLevelMapping implements Serializable {
    @Column(nullable = false, unique = true, length = 20)
    private String license;

    public Manager(String license) {
        this.license = license;
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
