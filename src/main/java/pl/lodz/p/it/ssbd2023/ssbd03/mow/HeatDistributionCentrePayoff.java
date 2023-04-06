package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "heat_distribution_centre_pay_off")
public class HeatDistributionCentrePayoff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private BigDecimal consumption;

    @Column(nullable = false)
    private BigDecimal consumptionCost;

    @Column(nullable = false)
    private BigDecimal heatingAreaFactor;

    public HeatDistributionCentrePayoff() {
    }
}
