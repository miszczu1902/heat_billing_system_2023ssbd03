package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account_confirmation_token")
@NamedQueries({
        @NamedQuery(name = "AccountConfirmationToken.getActivationTokenByTokenValue",
                query = "SELECT t FROM AccountConfirmationToken t WHERE tokenValue = :tokenValue"),
        @NamedQuery(name = "AccountConfirmationToken.findAllUnconfirmedAccounts",
                query = "SELECT t FROM AccountConfirmationToken t WHERE t.account.isActive IS FALSE AND t.account.registerDate <= :date"),
        @NamedQuery(name = "AccountConfirmationToken.findAllAccountsToSendReminder",
                query = "SELECT t FROM AccountConfirmationToken t WHERE t.isReminderSent IS FALSE AND t.account.isActive IS FALSE AND t.account.registerDate <= :date")
})
public class AccountConfirmationToken extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token_value", nullable = false, length = 10, updatable = false, unique = true)
    private String tokenValue;

    @Column(name = "expiration_date", nullable = false, updatable = false)
    private LocalDateTime expirationDate;

    @Setter
    @Column(name = "is_reminder_sent", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isReminderSent;


    @OneToOne
    @JoinColumn(name = "account_id", updatable = false, referencedColumnName = "id")
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountConfirmationToken that = (AccountConfirmationToken) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public AccountConfirmationToken(String tokenValue, Account account) {
        this.tokenValue = tokenValue;
        this.expirationDate = account.getRegisterDate().plusDays(1);
        this.account = account;
        this.isReminderSent = false;
    }
}
