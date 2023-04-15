package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;


import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@MappedSuperclass
public abstract class AbstractEntity {
    @Version
    protected long version;
// Zmienne nie używane w dotychczasowych zadaniach
//    protected LocalDateTime lastModificationDate;
//    protected String lastModificationAuthor;
//    protected String lastModificationOperation;
}
