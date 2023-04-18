package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    @Version
    protected Long version;
}
