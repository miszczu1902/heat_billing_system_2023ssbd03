package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "account")
public abstract class Account extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    protected Long id;

    @Setter
    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]$", message = "Invalid email format")
    @Column(nullable = false, unique = true)
    protected String email;

    @Setter
    @Pattern(regexp = "^[a-zA-Z0-9_]{6,}$", message = "Username must be at least 6 characters long and can only contain letters, digits, and underscore")
    @Column(nullable = false, length = 16)
    protected String username;

    @Setter
    @Column(nullable = false, length = 60)
    protected String password;

    @Setter
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected Boolean isEnable;

    @Setter
    @Column(nullable = false)
    protected LocalDateTime registerDate;

    @Setter
    @Column(nullable = false, columnDefinition = "VARCHAR DEFAULT 'PL'")
    protected String language_;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "login_data_id")
    protected LoginData loginData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_data_id")
    protected PersonalData personalData;
}
