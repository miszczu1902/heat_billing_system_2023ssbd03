package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;

import java.math.BigDecimal;
import java.util.List;

@Local
public interface BalanceService extends CommonManagerLocalInterface {

    MonthPayoff getUnitWarmCostReport();

    AnnualBalance getSelfReport(String placeId);

    AnnualBalance getUserReport(String placeId);

    List<AnnualBalance> getAllReports(int pageNumber, int pageSize, Long buildingId);

    AnnualBalance getSelfReports();

    List<HotWaterAdvance>  getSelfWaterAdvanceValue(Long placeId);

    List<HotWaterAdvance>  getUserWaterAdvanceValue(Long placeId, Integer year);

    List<HeatingPlaceAndCommunalAreaAdvance>  getSelfHeatingAdvanceValue(Long placeId);

    List<HeatingPlaceAndCommunalAreaAdvance>  getUserHeatingAdvanceValue(Long placeId, Integer year);

    BigDecimal getUserHeatingBalance();

    BigDecimal getSelfHeatingBalance();

    void createYearReports();

    void updateTotalCostYearReports();
}
