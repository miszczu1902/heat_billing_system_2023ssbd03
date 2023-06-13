package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitWarmCostReport {
    private BigDecimal pricePerCubicMeter;
    private BigDecimal pricePerSquareMeter;
    private Integer month;
    private Integer year;
}
