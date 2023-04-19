package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "login_data")
public class LoginData extends AbstractEntity implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Account id;

    @Setter
    @Column(name = "last_valid_login_date", nullable = false)
    private LocalDateTime lastValidLoginDate;

    @Setter
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    @Column(name = "last_valid_logic_address", nullable = false, length = 64)
    private String lastValidLogicAddress;

    @Setter
    @Column(name = "last_invalid_login_date", nullable = false)
    private LocalDateTime lastInvalidLoginDate;

    @Setter
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    @Column(name = "last_invalid_logic_address", nullable = false, length = 64)
    private String lastInvalidLogicAddress;

    @Setter
    @Min(value = 0)
    @Max(value = 3)
    @Column(name = "invalid_login_counter", nullable = false)
    private int invalidLoginCounter;
}
