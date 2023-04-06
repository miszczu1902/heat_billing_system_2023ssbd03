package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "hot_water_advance")
public final class HotWaterAdvance extends Advance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    BigDecimal hotWaterAdvanceValue;

    public HotWaterAdvance(Long id, LocalDate date, Place place, Long id1, BigDecimal hotWaterAdvanceValue) {
        super(id, date, place);
        this.id = id1;
        this.hotWaterAdvanceValue = hotWaterAdvanceValue;
    }
}
