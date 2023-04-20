package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "heating_place_and_communal_area_advance")
public final class HeatingPlaceAndCommunalAreaAdvance extends Advance implements Serializable {

    @DecimalMin(value = "0")
    @Column(name = "heating_place_advance_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal heatingPlaceAdvanceValue;

    @DecimalMin(value = "0")
    @Column(name = "heating_communal_area_advance_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal heatingCommunalAreaAdvanceValue;

    // Zapytać czy tu nie musi być DecimalMin i DecimalMax
    @Min(value = 0)
    @Max(value = 9)
    @Column(name = "advance_change_factor", nullable = false)
    private BigDecimal advanceChangeFactor;
}

