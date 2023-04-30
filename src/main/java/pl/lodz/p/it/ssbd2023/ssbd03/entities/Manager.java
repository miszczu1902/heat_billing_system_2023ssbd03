package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@DiscriminatorValue("MANAGER")
@Table(name = "manager",
        indexes = {
                @Index(name = "unique_license", columnList = "license", unique = true)
        }
)
public class Manager extends AccessLevelMapping implements Serializable {
    @Column(name = "license", nullable = false, length = 20)
    private String license;
}
