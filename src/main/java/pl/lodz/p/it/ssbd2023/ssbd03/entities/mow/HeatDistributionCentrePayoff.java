package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AbstractEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "heat_distribution_centre_pay_off")
public class HeatDistributionCentrePayoff extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_", nullable = false)
    private LocalDate date;

    @DecimalMin(value = "0")
    @Column(name = "coonsumption", nullable = false, precision = 10, scale = 2)
    private BigDecimal consumption;

    @DecimalMin(value = "0")
    @Column(name = "consumption_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal consumptionCost;

    @DecimalMin(value = "0")
    @Column(name = "heating_area_factor", nullable = false, precision = 10, scale = 2)
    private BigDecimal heatingAreaFactor;
}
