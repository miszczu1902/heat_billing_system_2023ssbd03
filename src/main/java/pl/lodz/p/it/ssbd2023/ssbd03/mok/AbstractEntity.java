package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class AbstractEntity {
    private int version;
    private LocalDateTime lastModificationDate;
    private String lastModificationAuthor;
    private String lastModificationOperation;
}
