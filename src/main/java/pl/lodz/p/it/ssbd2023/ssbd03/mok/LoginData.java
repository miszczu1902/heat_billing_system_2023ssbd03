package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "login_data")
public class LoginData {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime lastValidLoginDate;

    @Column(nullable = false)
    private String lastValidLogicAddress;

    @Column(nullable = false)
    private LocalDateTime lastInvalidLoginDate;

    @Column(nullable = false)
    private String lastInvalidLogicAddress;

    @Column(nullable = false)
    private int invalidLoginCounter;

}
