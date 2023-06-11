package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AnnualBalance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatingPlaceAndCommunalAreaAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.MonthPayoff;

import java.math.BigDecimal;
import java.util.List;

@Local
public interface BalanceService extends CommonManagerLocalInterface {

    MonthPayoff getUnitWarmCostReport();

    AnnualBalance getSelfReport(String placeId);

    AnnualBalance getUserReport(String placeId);

    List<AnnualBalance> getAllReports(int pageNumber, int pageSize, Long buildingId);

    AnnualBalance getSelfReports();

    HotWaterAdvance getSelfWaterAdvanceValue();

    HotWaterAdvance getSelfWaterAdvance();

    HotWaterAdvance getUserWaterAdvanceValue();

    HotWaterAdvance getUserWaterAdvance();

    HeatingPlaceAndCommunalAreaAdvance getSelfHeatingAdvanceValue();

    HeatingPlaceAndCommunalAreaAdvance getSelfHeatingAdvance();

    HeatingPlaceAndCommunalAreaAdvance getUserHeatingAdvanceValue();

    HeatingPlaceAndCommunalAreaAdvance getUserHeatingAdvance();

    BigDecimal getUserHeatingBalance();

    BigDecimal getSelfHeatingBalance();

    void createYearReports();

    void updateTotalCostYearReports();
}
