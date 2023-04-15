package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AbstractEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "heat_distribution_centre_pay_off")
public class HeatDistributionCentrePayoff extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heat_distribution_centre_pay_off_id")
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consumption;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consumptionCost;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal heatingAreaFactor;


}
