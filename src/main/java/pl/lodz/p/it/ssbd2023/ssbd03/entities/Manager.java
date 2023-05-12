package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Data
@DiscriminatorValue("MANAGER")
@Table(name = "manager",
        indexes = {
                @Index(name = "unique_license", columnList = "license", unique = true)
        }
)
@NamedQueries({
        @NamedQuery(name = "Manager.findByLicenseAndWithoutUsername", query = "SELECT d FROM Manager d WHERE d.license = :license AND d.account.username != :username")
})
public class Manager extends AccessLevelMapping implements Serializable {
    @Column(name = "license", nullable = false, length = 20)
    private String license;
}
