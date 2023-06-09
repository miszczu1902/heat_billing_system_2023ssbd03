package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "heating_place_and_communal_area_advance")
@NamedQueries({
        @NamedQuery(name = "HeatingPlaceAndCommunalAreaAdvance.getAllHeatingPlaceAndCommunalAreaAdvances",
                query = "SELECT a FROM HeatingPlaceAndCommunalAreaAdvance a WHERE a.place.building.id = :buildingId AND a.date >= :date ORDER BY a.date DESC")
})
public final class HeatingPlaceAndCommunalAreaAdvance extends Advance implements Serializable {

    @DecimalMin(value = "0")
    @Column(name = "heating_place_advance_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal heatingPlaceAdvanceValue;

    @DecimalMin(value = "0")
    @Column(name = "heating_communal_area_advance_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal heatingCommunalAreaAdvanceValue;

    @Setter
    @Min(value = 0)
    @Max(value = 9)
    @Column(name = "advance_change_factor", nullable = false)
    private BigDecimal advanceChangeFactor;

    public HeatingPlaceAndCommunalAreaAdvance(LocalDate date, Place place, BigDecimal heatingPlaceAdvanceValue, BigDecimal heatingCommunalAreaAdvanceValue, BigDecimal advanceChangeFactor) {
        super(date, place);
        this.heatingPlaceAdvanceValue = heatingPlaceAdvanceValue;
        this.heatingCommunalAreaAdvanceValue = heatingCommunalAreaAdvanceValue;
        this.advanceChangeFactor = advanceChangeFactor;
    }
}

