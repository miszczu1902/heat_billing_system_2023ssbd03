package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatDistributionCentre;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterEntry;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Manager;

import java.math.BigDecimal;

@Local
public interface HeatDistributionCentreService extends CommonManagerLocalInterface {

    Void getHeatDistributionCentreParameters();

    void insertAdvanceChangeFactor(BigDecimal heatingAreaFactorValue, Long buildingId);

    void insertConsumption(BigDecimal consumptionValue, Long placeId);

    void modifyConsumption(BigDecimal consumptionValue, Long placeId, Long version, String etag);

    void addConsumptionFromInvoice(BigDecimal consumption, BigDecimal consumptionCost, BigDecimal heatingAreaFactor);

    HeatDistributionCentre getHeatDistributionCentre(Long id);

    HotWaterEntry getHotWaterEntry(Long hotWaterEntryId);
}
