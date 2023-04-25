package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "account",
        indexes = {
                @Index(name = "unique_email", columnList = "email", unique = true),
                @Index(name = "unique_username", columnList = "username", unique = true)
}
)
public class Account extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,10}$", message = "Invalid email format")
    @Column(name = "email", nullable = false)
    private String email;

    @Setter
    @Pattern(regexp = "^[a-zA-Z0-9_]{6,}$", message = "Username must be at least 6 characters long and can only contain letters, digits, and underscore")
    @Column(name = "username", nullable = false, length = 16)
    private String username;

    @Setter
    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Setter
    @Column(name = "is_enable", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isEnable;

    @Setter
    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isActive;

    @Setter
    @Column(name = "register_date", nullable = false)
    private LocalDateTime registerDate;

    @Setter
    @Column(name = "language_", nullable = false, columnDefinition = "VARCHAR DEFAULT 'PL'")
    private String language_;

    @Getter
    @OneToMany(mappedBy = "account", cascade = CascadeType.PERSIST)
    private List<AccessLevelMapping> accessLevels = new ArrayList<>();
}
