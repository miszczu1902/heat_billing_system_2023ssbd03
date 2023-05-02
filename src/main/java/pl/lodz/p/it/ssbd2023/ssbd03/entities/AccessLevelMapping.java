package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "access_level")
@Entity
@Table(name = "access_level_mapping",
        indexes = {
                @Index(name = "access_level_mapping_account_id", columnList = "account_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "access_level_account_unique_constraint", columnNames = {"account_id", "access_level"})
        })
public abstract class AccessLevelMapping extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "account_id", updatable = false, referencedColumnName = "id")
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessLevelMapping that = (AccessLevelMapping) o;
        return Objects.equals(id, that.id);
    }

    @Column(name = "access_level", updatable = false, insertable = false)
    private String accessLevel;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
