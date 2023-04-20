package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "hot_water_entry",
        indexes = {
                @Index(name = "hot_water_entry_place_id", columnList = "place_id"),
                @Index(name = "hot_water_entry_manager_id", columnList = "manager_id")
        })
public class HotWaterEntry extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_", nullable = false)
    private LocalDate date;

    @Setter
    @DecimalMin(value = "0")
    @Column(name = "entry_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal entryValue;

    @ManyToOne
    @JoinColumn(name = "place_id", updatable = false, referencedColumnName = "id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "manager_id", updatable = false, referencedColumnName = "id")
    private Manager manager;
}
