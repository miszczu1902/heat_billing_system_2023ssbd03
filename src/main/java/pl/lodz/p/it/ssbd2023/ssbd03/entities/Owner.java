package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@DiscriminatorValue("OWNER")
@Table(name = "owner",
        indexes = {
                @Index(name = "unique_phone_number", columnList = "phone_number", unique = true)
        }
)
@NamedQueries({
        @NamedQuery(name = "Owner.findByPhoneNumber", query = "SELECT d FROM Owner d WHERE d.phoneNumber = :phoneNumber"),
})
public class Owner extends AccessLevelMapping implements Serializable {
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
}