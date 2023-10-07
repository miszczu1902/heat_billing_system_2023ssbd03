package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;

import java.math.BigDecimal;
import java.util.List;

@Local
public interface BalanceService extends CommonManagerLocalInterface {

    BigDecimal getUnitWarmCostReportHotWater();

    BigDecimal getUnitWarmCostReportCentralHeating();

    AnnualBalance getYearReport(Long reportId);

    AnnualBalance getOwnerYearReport(Long reportId);

    List<AnnualBalance> getAllReports(int pageNumber, int pageSize, Long buildingId);

    List<AnnualBalance> getSelfReports(int pageNumber, int pageSize);

    List<HotWaterAdvance> getSelfWaterAdvanceValue(Long placeId, Integer year);

    List<HotWaterAdvance> getUserWaterAdvanceValue(Long placeId, Integer year);

    List<HeatingPlaceAndCommunalAreaAdvance> getSelfHeatingAdvanceValue(Long placeId, Integer year);

    List<HeatingPlaceAndCommunalAreaAdvance> getUserHeatingAdvanceValue(Long placeId, Integer year);

    void createYearReports();

    void updateTotalCostYearReports();
}
