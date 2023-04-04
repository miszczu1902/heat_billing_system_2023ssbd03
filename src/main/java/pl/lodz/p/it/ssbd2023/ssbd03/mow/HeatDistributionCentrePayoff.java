package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class HeatDistributionCentrePayoff {

    private Integer id;
    private LocalDate date;
    private BigDecimal consumption;
    private BigDecimal consumptionCost;
    private BigDecimal heatingAreaFactor;

}
