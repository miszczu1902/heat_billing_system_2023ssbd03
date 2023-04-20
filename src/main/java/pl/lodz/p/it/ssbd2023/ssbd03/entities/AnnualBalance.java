package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "annual_balance",
        indexes = {
                @Index(name = "annual_balance_place_id", columnList = "place_id")
        })
public class AnnualBalance extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 2021)
    @Column(name = "year_", nullable = false)
    private Short year;

    @DecimalMin(value = "0")
    @Column(name = "total_hot_water_advance", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHotWaterAdvance;

    @DecimalMin(value = "0")
    @Column(name = "total_heating_place_advance", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHeatingPlaceAdvance;

    @DecimalMin(value = "0")
    @Column(name = "total_heating_communal_area_advance", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHeatingCommunalAreaAdvance;

    @DecimalMin(value = "0")
    @Column(name = "total_hot_water_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHotWaterCost;

    @DecimalMin(value = "0")
    @Column(name = "total_heating_place_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHeatingPlaceCost;

    @DecimalMin(value = "0")
    @Column(name = "total_heating_communal_area_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHeatingCommunalAreaCost;

    @ManyToOne
    @JoinColumn(name = "place_id", updatable = false, referencedColumnName = "id")
    private Place place;
}
