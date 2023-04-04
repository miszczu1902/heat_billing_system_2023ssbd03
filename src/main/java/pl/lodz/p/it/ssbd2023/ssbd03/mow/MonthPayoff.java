package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class MonthPayoff {

    private Integer id;
    private LocalDate payoffDate;
    private BigDecimal waterHeatingUnitCost;
    private BigDecimal centralHeatingUnitCost;
    private BigDecimal hotWaterConsumption;

}
