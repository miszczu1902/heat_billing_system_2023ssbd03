package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "admin")
public class Admin extends AccessLevelMapping implements Serializable {
}
