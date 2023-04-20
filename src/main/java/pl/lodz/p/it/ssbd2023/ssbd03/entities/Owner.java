package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
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
@Table(name = "owner",
        indexes = {
                @Index(name = "unique_phoneNumber", columnList = "phoneNumber", unique = true)
        }
)
public class Owner extends AccessLevelMapping implements Serializable {
    @Column(name = "phone_number", nullable = false, length = 9)
    private String phoneNumber;
}