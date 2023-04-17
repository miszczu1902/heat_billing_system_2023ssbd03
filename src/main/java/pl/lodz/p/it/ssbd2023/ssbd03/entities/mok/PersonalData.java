package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.Account;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "personal_data")
public class PersonalData extends AbstractEntity implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account id;

    @Setter
    @Column(nullable = false, length = 32)
    private String firstName;

    @Setter
    @Column(nullable = false, length = 32)
    private String surname;
}
