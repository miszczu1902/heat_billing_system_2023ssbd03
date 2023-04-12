package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "login_data")
public class LoginData extends AbstractEntity{
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    @Column(name = "login_data")
    private Account id;

    @Setter
    @Column(nullable = false)
    private LocalDateTime lastValidLoginDate;

    @Setter
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    @Column(nullable = false, length = 64)
    private String lastValidLogicAddress;

    @Setter
    @Column(nullable = false)
    private LocalDateTime lastInvalidLoginDate;

    @Setter
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    @Column(nullable = false, length = 64)
    private String lastInvalidLogicAddress;

    @Setter
    @Column(nullable = false)
    private int invalidLoginCounter;

}
