package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.common.EntityListener;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(value = EntityListener.class)
public abstract class AbstractEntity {
    @Version
    private Long version;

    @Setter
    @Column(name = "creation_date_time", nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    @Setter
    @Column(name = "last_modification_date_time")
    private LocalDateTime lastModificationDateTime;

    @Setter
    @OneToOne
    @JoinColumn(name = "created_by", updatable = false)
    private Account createdBy;

    @Setter
    @OneToOne
    @JoinColumn(name = "last_modified_by")
    private Account lastModifiedBy;
}
