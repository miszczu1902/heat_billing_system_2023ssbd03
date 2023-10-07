package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@DiscriminatorValue("OWNER")
@Table(name = "owner",
        indexes = {
                @Index(name = "unique_phone_number", columnList = "phone_number", unique = true)
        }
)
@NamedQueries({
        @NamedQuery(name = "Owner.findByPhoneNumberAndWithoutUsername", query = "SELECT d FROM Owner d WHERE d.phoneNumber = :phoneNumber AND d.account.username != :username"),
        @NamedQuery(name = "Owner.findByPhoneNumber", query = "SELECT d FROM Owner d WHERE d.phoneNumber = :phoneNumber"),
        @NamedQuery(name = "Owner.findById", query = "SELECT k FROM Owner k WHERE k.id = :id"),
        @NamedQuery(name = "Owner.findAllOwners", query = "SELECT k FROM Owner k"),
        @NamedQuery(name = "Owner.findByUsername", query = "SELECT d FROM Owner d WHERE d.account.username = :username")
})
public class Owner extends AccessLevelMapping implements Serializable, Signable {
    @Column(name = "phone_number", nullable = false, length = 9)
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Owner owner = (Owner) o;
        return Objects.equals(phoneNumber, owner.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), phoneNumber);
    }

    @Override
    public String messageToSign() {
        return phoneNumber
                .concat(getId().toString())
                .concat(getVersion().toString());
    }
}