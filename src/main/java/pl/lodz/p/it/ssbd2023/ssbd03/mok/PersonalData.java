package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "personal_data")
public class PersonalData extends AbstractEntity{
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
