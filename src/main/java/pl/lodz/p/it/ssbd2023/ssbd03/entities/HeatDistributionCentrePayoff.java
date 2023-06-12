package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "heat_distribution_centre_pay_off",
        indexes = {
                @Index(name = "heat_distribution_centre_pay_off_heat_distribution_centre_id", columnList = "heat_distribution_centre_id"),
                @Index(name = "heat_distribution_centre_pay_off_manager_id", columnList = "manager_id")
        })
@NamedQueries({
        @NamedQuery(name = "HeatDistributionCentrePayoff.getPayoffByDate",
                query = "SELECT k FROM HeatDistributionCentrePayoff k WHERE YEAR(k.date) = :year AND MONTH(k.date)= :month"),
        @NamedQuery(name = "HeatDistributionCentrePayoff.findAllHeatDistributionCentrePayoff",
                query = "SELECT k FROM HeatDistributionCentrePayoff k"),
        @NamedQuery(name = "HeatDistributionCentrePayoff.getLatestHeatDistributionCentrePayoff",
                query = "SELECT k FROM HeatDistributionCentrePayoff k ORDER BY k.creationDateTime DESC")
})
public class HeatDistributionCentrePayoff extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_", nullable = false)
    private LocalDate date;

    @DecimalMin(value = "0")
    @Column(name = "consumption", nullable = false, precision = 10, scale = 2)
    private BigDecimal consumption;

    @DecimalMin(value = "0")
    @Column(name = "consumption_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal consumptionCost;

    @DecimalMin(value = "0")
    @Column(name = "heating_area_factor", nullable = false, precision = 3, scale = 2)
    private BigDecimal heatingAreaFactor;

    @ManyToOne
    @JoinColumn(name = "heat_distribution_centre_id", updatable = false, referencedColumnName = "id")
    private HeatDistributionCentre heatDistributionCentre;

    @ManyToOne
    @JoinColumn(name = "manager_id", updatable = false, referencedColumnName = "id")
    private Manager manager;

    public HeatDistributionCentrePayoff(BigDecimal consumption, BigDecimal consumptionCost, LocalDate time, BigDecimal heatingAreaFactor, Manager manager, HeatDistributionCentre heatDistributionCentre) {
        this.consumption= consumption;
        this.consumptionCost = consumptionCost;
        this.date = time;
        this.heatingAreaFactor = heatingAreaFactor;
        this.manager = manager;
        this.heatDistributionCentre = heatDistributionCentre;
    }
}
