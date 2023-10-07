package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reset_password_token")
@NamedQueries({
        @NamedQuery(name = "ResetPasswordToken.getResetPasswordTokenByTokenValue",
                query = "SELECT t FROM ResetPasswordToken t WHERE tokenValue = :tokenValue"),
        @NamedQuery(
                name = "ResetPasswordToken.getOlderResetPasswordToken",
                query = "SELECT token FROM ResetPasswordToken token WHERE token.expirationDate < :currentTime"
        )
})
public class ResetPasswordToken extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token_value", nullable = false, length = 10, updatable = false, unique = true)
    private String tokenValue;

    @Column(name = "expiration_date", nullable = false, updatable = false)
    private LocalDateTime expirationDate;

    @OneToOne
    @JoinColumn(name = "account_id", updatable = false, referencedColumnName = "id")
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResetPasswordToken that = (ResetPasswordToken) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public ResetPasswordToken(String tokenValue, Account account) {
        this.tokenValue = tokenValue;
        this.expirationDate = LocalDateTime.now(TIME_ZONE).plusMinutes(20);
        this.account = account;
    }
}