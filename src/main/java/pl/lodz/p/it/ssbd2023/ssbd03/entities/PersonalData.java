package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "personal_data")
public class PersonalData extends AbstractEntity implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Account id;

    @Setter
    @Column(name = "first_name", nullable = false, length = 32)
    private String firstName;

    @Setter
    @Column(name = "surname", nullable = false, length = 32)
    private String surname;
}
