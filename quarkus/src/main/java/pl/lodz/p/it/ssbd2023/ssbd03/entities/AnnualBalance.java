package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.AbstractEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "annual_balance",
        indexes = {
                @Index(name = "annual_balance_place_id", columnList = "place_id")
        })
@NamedNativeQueries({
        @NamedNativeQuery(name = "AnnualBalance.findAllBalancesByPlacesId", query = "SELECT k FROM AnnualBalance k WHERE k.place.id IN :ids"),
        @NamedNativeQuery(name = "AnnualBalance.findAllBalancesByOwnerUsername", query = "SELECT k FROM AnnualBalance k WHERE k.place.owner.account.username = :username"),
        @NamedNativeQuery(name = "AnnualBalance.findAllBalancesByYear", query = "SELECT k FROM AnnualBalance k WHERE k.year = :year"),
        @NamedNativeQuery(name = "AnnualBalance.findBalanceById", query = "SELECT k FROM AnnualBalance k WHERE k.id = :id")
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

    @Setter
    @DecimalMin(value = "0")
    @Column(name = "total_hot_water_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHotWaterCost;

    @Setter
    @DecimalMin(value = "0")
    @Column(name = "total_heating_place_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHeatingPlaceCost;

    @Setter
    @DecimalMin(value = "0")
    @Column(name = "total_heating_communal_area_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHeatingCommunalAreaCost;

    @ManyToOne
    @JoinColumn(name = "place_id", updatable = false, referencedColumnName = "id")
    private Place place;

    public AnnualBalance(Short year, BigDecimal totalHotWaterAdvance, BigDecimal totalHeatingPlaceAdvance, BigDecimal totalHeatingCommunalAreaAdvance, BigDecimal totalHotWaterCost, BigDecimal totalHeatingPlaceCost, BigDecimal totalHeatingCommunalAreaCost, Place place) {
        this.year = year;
        this.totalHotWaterAdvance = totalHotWaterAdvance;
        this.totalHeatingPlaceAdvance = totalHeatingPlaceAdvance;
        this.totalHeatingCommunalAreaAdvance = totalHeatingCommunalAreaAdvance;
        this.totalHotWaterCost = totalHotWaterCost;
        this.totalHeatingPlaceCost = totalHeatingPlaceCost;
        this.totalHeatingCommunalAreaCost = totalHeatingCommunalAreaCost;
        this.place = place;
    }
}
