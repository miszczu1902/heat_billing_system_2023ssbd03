package pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;


import java.io.Serializable;
import java.util.Objects;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personal_data")
@NamedQueries({
        @NamedQuery(name = "PersonalData.findByUsername", query = "SELECT k FROM PersonalData k WHERE k.id.username = :username")
})
public class PersonalData extends AbstractEntity implements Serializable, Signable {
    @Id
    @OneToOne
    @JoinColumn(name = "id", updatable = false, referencedColumnName = "id")
    private Account id;

    @Setter
    @Column(name = "first_name", nullable = false, length = 32)
    @Pattern(regexp = "^[A-Za-z'-]+$", message = "Invalid first name format")
    private String firstName;

    @Setter
    @Column(name = "surname", nullable = false, length = 32)
    @Pattern(regexp = "^[A-Za-z'-]+$", message = "Invalid surname format")
    private String surname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonalData that = (PersonalData) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String messageToSign() {
        return firstName
                .concat(surname)
                .concat(getId().getId().toString())
                .concat(getVersion().toString());
    }
}
