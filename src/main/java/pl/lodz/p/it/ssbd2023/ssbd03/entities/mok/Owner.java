package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;


@Entity
@Getter
@Setter
@Table(name = "owner")
@DiscriminatorValue("OWNER")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner extends AccessLevelMapping implements Serializable {
    @Column(name = "phone_number", nullable = false, unique = true, length = 9)
    private String phoneNumber;
}