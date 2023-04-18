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
@DiscriminatorValue("MANAGER")
@Table(name = "manager")
public class Manager extends AccessLevelMapping implements Serializable {
    @Column(name = "license", nullable = false, unique = true, length = 20)
    private String license;

    public Manager(String license) {
        this.license = license;
    }
}
