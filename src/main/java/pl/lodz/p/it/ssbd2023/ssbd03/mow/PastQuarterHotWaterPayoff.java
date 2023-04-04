package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class PastQuarterHotWaterPayoff {

    private Integer id;

    @Setter
    private BigDecimal averageConsumption;

    @Setter
    private Integer daysNumberInQuarter;

}
