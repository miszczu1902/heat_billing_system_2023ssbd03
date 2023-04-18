package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "admin")
public class Admin extends AccessLevelMapping implements Serializable {
}
