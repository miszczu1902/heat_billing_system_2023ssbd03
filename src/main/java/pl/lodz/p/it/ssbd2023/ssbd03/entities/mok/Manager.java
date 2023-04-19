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
@AllArgsConstructor
@Table(name = "manager")
@DiscriminatorValue("MANAGER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager extends AccessLevelMapping implements Serializable {

    @Column(name = "license", nullable = false, unique = true, length = 20)
    private String license;

}
