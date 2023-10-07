package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "hot_water_advance")
@NamedNativeQueries({
        @NamedNativeQuery(name = "HotWaterAdvance.findByDate", query = "SELECT d FROM HotWaterAdvance d WHERE d.place.id = :placeId AND d.date = :date_"),
})
public class HotWaterAdvance extends Advance implements Serializable {
    @Setter
    @DecimalMin(value = "0")
    @Column(name = "hot_water_advance_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal hotWaterAdvanceValue;

    public HotWaterAdvance(LocalDate date, Place place, BigDecimal hotWaterAdvanceValue) {
        super(date, place);
        this.hotWaterAdvanceValue = hotWaterAdvanceValue;
    }
}
