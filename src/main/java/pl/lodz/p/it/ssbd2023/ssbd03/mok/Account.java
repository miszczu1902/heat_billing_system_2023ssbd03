package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "account")
public abstract class Account {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Column(nullable = false)
    protected Boolean isEnable;

    @Column(nullable = false)
    protected LocalDateTime registerDate;

    @Column(nullable = false)
    protected String language_;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "login_data_id")
    protected LoginData loginData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_data_id")
    protected PersonalData personalData;
}
