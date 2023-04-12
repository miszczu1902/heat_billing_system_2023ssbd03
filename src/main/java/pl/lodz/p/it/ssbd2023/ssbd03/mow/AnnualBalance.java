package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.AbstractEntity;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "annual_balance")
public class AnnualBalance extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annual_balance_id")
    private Long id;

    @Column(nullable = false, name = "year_")
    private Short year;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHotWaterAdvance;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHeatingPlaceAdvance;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHeatingCommunalAreaAdvance;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHotWaterCost;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHeatingPlaceCost;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHeatingCommunalAreaCost;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    private Place place;

    public void updateTotalAdvance(BigDecimal hotWater, BigDecimal heatingPlace, BigDecimal heatingCommunalArea) {

    }

    public void updateTotalCost(BigDecimal hotWater, BigDecimal heatingPlace, BigDecimal heatingCommunalArea) {

    }
}
