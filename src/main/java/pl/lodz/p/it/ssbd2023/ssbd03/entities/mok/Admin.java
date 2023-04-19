package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "admin")
@DiscriminatorValue("ADMIN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends AccessLevelMapping implements Serializable {
}
