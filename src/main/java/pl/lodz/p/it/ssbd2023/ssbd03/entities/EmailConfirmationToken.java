package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_confirmation_token")
@NamedQueries({
        @NamedQuery(name = "EmailConfirmationToken.getActivationTokenByTokenValue",
                query = "SELECT t FROM EmailConfirmationToken t WHERE tokenValue = :tokenValue"),
        @NamedQuery(
                name = "EmailConfirmationToken.findAllUnconfirmedEmails",
                query = "SELECT token FROM EmailConfirmationToken token WHERE token.expirationDate < :currentTime"
        )
})
public class EmailConfirmationToken extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token_value", nullable = false, length = 10, updatable = false, unique = true)
    private String tokenValue;

    @Column(name = "expiration_date", nullable = false, updatable = false)
    private LocalDateTime expirationDate;

    @Setter
    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,10}$", message = "Invalid email format")
    @Column(name = "email", nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(name = "account_id", updatable = false, referencedColumnName = "id")
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailConfirmationToken that = (EmailConfirmationToken) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public EmailConfirmationToken(String tokenValue, String email, Account account) {
        this.tokenValue = tokenValue;
        this.expirationDate = LocalDateTime.now(TIME_ZONE).plusDays(1);
        this.email = email;
        this.account = account;
    }
}
