package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;

import java.math.BigDecimal;

@Local
public interface HeatDistributionCentreService extends CommonManagerLocalInterface {

    Void getHeatDistributionCentreParameters();

    void modifyHeatingAreaFactor(BigDecimal heatingAreaFactorValue);

    void modifyConsumption(BigDecimal consumptionValue);

    void modifyConsumptionCost(BigDecimal consumptionCostValue);

}
