package pl.lodz.p.it.ssbd2023.ssbd03.mok.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.entities.Account;

@Getter
@Entity
@Table(name = "personal_data")
public class PersonalData extends AbstractEntity {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    @Column(name = "personal_data_id")
    private Account id;

    @Setter
    @Column(nullable = false, length = 32)
    private String firstName;

    @Setter
    @Column(nullable = false, length = 32)
    private String surname;
}
