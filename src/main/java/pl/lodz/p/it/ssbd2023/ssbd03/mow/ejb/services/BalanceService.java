package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AnnualBalance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.MonthPayoff;

@Local
public interface BalanceService extends CommonManagerLocalInterface {

    MonthPayoff getUnitWarmCostReport();

    AnnualBalance getSelfReport(String placeId);

    AnnualBalance getUserReport(String placeId);

    AnnualBalance getAllReports();
}
