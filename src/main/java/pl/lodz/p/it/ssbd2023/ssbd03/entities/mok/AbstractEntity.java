package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@MappedSuperclass
public abstract class AbstractEntity {
    @Version
    private Long version;
}
