package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class AnnualBalance {
    Integer id;
    Integer year;
    BigDecimal totalHotWaterAdvance;
    BigDecimal totalHeatingCommunalAreaAdvance;
    BigDecimal totalHotWaterCost;
    BigDecimal totalHeatingPlaceCost;
    BigDecimal totalHeatingCommunalAreaCost;

    public void updateTotalAdvance(BigDecimal hotWater, BigDecimal heatingPlace, BigDecimal heatingCommunalArea) {

    }

    public void updateTotalCost(BigDecimal hotWater, BigDecimal heatingPlace, BigDecimal heatingCommunalArea) {

    }
}
