package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "LoginData.findById", query = "SELECT d FROM LoginData d WHERE d.id = :id")
})
@Table(name = "login_data")
public class LoginData extends AbstractEntity implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "id", updatable = false, referencedColumnName = "id")
    private Account id;

    @Setter
    @Column(name = "last_valid_login_date")
    private LocalDateTime lastValidLoginDate;

    @Setter
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    @Column(name = "last_valid_logic_address", length = 15)
    private String lastValidLogicAddress;

    @Setter
    @Column(name = "last_invalid_login_date")
    private LocalDateTime lastInvalidLoginDate;

    @Setter
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    @Column(name = "last_invalid_logic_address", length = 15)
    private String lastInvalidLogicAddress;

    @Setter
    @Min(value = 0)
    @Max(value = 3)
    @Column(name = "invalid_login_counter", columnDefinition = "INTEGER DEFAULT '0'")
    private Integer invalidLoginCounter;

    public LoginData(Account id) {
        this.id = id;
        this.invalidLoginCounter = 0;
    }
}
