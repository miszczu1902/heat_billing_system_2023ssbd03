package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class AbstractEntity {
    protected long version;
//    protected LocalDateTime lastModificationDate;
//    protected String lastModificationAuthor;
//    protected String lastModificationOperation;
}
