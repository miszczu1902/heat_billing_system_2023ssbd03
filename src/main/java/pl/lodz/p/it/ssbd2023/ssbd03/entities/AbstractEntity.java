package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class AbstractEntity {
    @Version
    private Long version;

    @Column(name = "creation_date_time", nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    @Column(name = "last_modification_date_time")
    private LocalDateTime lastModificationDateTime;

    @PrePersist
    private void init() {
        this.creationDateTime = LocalDateTime.now();
    }

    @PreUpdate
    private void initModification() {
        this.lastModificationDateTime = LocalDateTime.now();
    }
}
