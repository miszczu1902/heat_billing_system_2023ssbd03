package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@DiscriminatorValue("OWNER")
@Table(name = "owner")
public class Owner extends AccessLevelMapping implements Serializable {
    @Column(name = "phone_number", nullable = false, unique = true, length = 9)
    private String phoneNumber;

    public Owner(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}