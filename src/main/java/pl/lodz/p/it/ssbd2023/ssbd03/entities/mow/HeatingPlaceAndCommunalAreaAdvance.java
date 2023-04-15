package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Table(name = "heating_place_and_communal_area_advance")
public final class HeatingPlaceAndCommunalAreaAdvance extends Advance {
    @Column(nullable = false)
    private BigDecimal heatingPlaceAdvanceValue;

    @DecimalMin(value = "0")
    @Column(nullable = false)
    private BigDecimal heatingCommunalAreaAdvanceValue;

    @Min(value = 0)
    @Max(value = 1)
    @Column(nullable = false)
    private Float advanceChangeFactor;

    public HeatingPlaceAndCommunalAreaAdvance(Long id, LocalDate date, Place place, BigDecimal heatingPlaceAdvanceValue, BigDecimal heatingCommunalAreaAdvanceValue, Float advanceChangeFactor) {
        super(id, date, place);
        this.heatingPlaceAdvanceValue = heatingPlaceAdvanceValue;
        this.heatingCommunalAreaAdvanceValue = heatingCommunalAreaAdvanceValue;
        this.advanceChangeFactor = advanceChangeFactor;
    }
}

