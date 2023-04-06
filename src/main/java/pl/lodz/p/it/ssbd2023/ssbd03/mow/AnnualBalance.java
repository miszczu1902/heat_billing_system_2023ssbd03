package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "annual_balance")
public class AnnualBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private BigDecimal totalHotWaterAdvance;

    @Column(nullable = false)
    private BigDecimal totalHeatingPlaceAdvance;

    @Column(nullable = false)
    private BigDecimal totalHeatingCommunalAreaAdvance;

    @Column(nullable = false)
    private BigDecimal totalHotWaterCost;

    @Column(nullable = false)
    private BigDecimal totalHeatingPlaceCost;

    @Column(nullable = false)
    private BigDecimal totalHeatingCommunalAreaCost;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    private Place place;

    public void updateTotalAdvance(BigDecimal hotWater, BigDecimal heatingPlace, BigDecimal heatingCommunalArea) {

    }

    public void updateTotalCost(BigDecimal hotWater, BigDecimal heatingPlace, BigDecimal heatingCommunalArea) {

    }
}
