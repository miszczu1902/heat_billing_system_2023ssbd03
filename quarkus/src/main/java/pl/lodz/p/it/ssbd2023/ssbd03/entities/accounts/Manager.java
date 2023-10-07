package pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

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
@NamedNativeQueries({
        @NamedNativeQuery(name = "Manager.findByUsername", query = "SELECT d FROM Manager d WHERE d.account.username = :username"),
        @NamedNativeQuery(name = "Manager.findByLicenseAndWithoutUsername",
                query = "SELECT d FROM Manager d WHERE d.license = :license AND d.account.username != :username"),
        @NamedNativeQuery(name = "Manager.findAllManagers", query = "SELECT k FROM Manager k"),
})
public class Manager extends AccessLevelMapping implements Serializable, Signable {
    @Column(name = "license", nullable = false, length = 20)
    private String license;

    @Override
    public String messageToSign() {
        return super.getAccount().messageToSign()
                .concat(getLicense())
                .concat(getId().toString())
                .concat(getVersion().toString());
    }
}
