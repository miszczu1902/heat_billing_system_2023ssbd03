package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.Account;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "access_level_mapping")
public class AccessLevelMapping extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_level_mapping_id")
    private Long id;

    @Column(nullable = false, length = 7)
    private String accessLevel;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;
}
