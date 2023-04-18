package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "heating_place_and_communal_area_advance")
public final class HeatingPlaceAndCommunalAreaAdvance extends Advance implements Serializable {
    @Column(name = "heating_place_advance_value", nullable = false)
    private BigDecimal heatingPlaceAdvanceValue;

    @DecimalMin(value = "0")
    @Column(name = "heating_communal_area_advance_value", nullable = false)
    private BigDecimal heatingCommunalAreaAdvanceValue;

    @Min(value = 0)
    @Max(value = 9)
    @Column(name = "advance_change_factor", nullable = false)
    private BigDecimal advanceChangeFactor;

    public HeatingPlaceAndCommunalAreaAdvance(Long id, LocalDate date, Place place, BigDecimal heatingPlaceAdvanceValue, BigDecimal heatingCommunalAreaAdvanceValue, BigDecimal advanceChangeFactor) {
        super(id, date, place);
        this.heatingPlaceAdvanceValue = heatingPlaceAdvanceValue;
        this.heatingCommunalAreaAdvanceValue = heatingCommunalAreaAdvanceValue;
        this.advanceChangeFactor = advanceChangeFactor;
    }
}

