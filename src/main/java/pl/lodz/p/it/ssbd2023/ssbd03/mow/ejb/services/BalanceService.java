package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Local;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AnnualBalance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatingPlaceAndCommunalAreaAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.MonthPayoff;

@Local
public interface BalanceService extends CommonManagerLocalInterface {

    MonthPayoff getUnitWarmCostReport();

    AnnualBalance getSelfReport(String placeId);

    AnnualBalance getUserReport(String placeId);

    AnnualBalance getAllReports();

    AnnualBalance getSelfReports();

    HotWaterAdvance getSelfWaterAdvanceValue();

    HotWaterAdvance getSelfWaterAdvance();

    HotWaterAdvance getUserWaterAdvanceValue();

    HotWaterAdvance getUserWaterAdvance();

    HeatingPlaceAndCommunalAreaAdvance getSelfHeatingAdvanceValue();

    HeatingPlaceAndCommunalAreaAdvance getSelfHeatingAdvance();

    HeatingPlaceAndCommunalAreaAdvance getUserHeatingAdvanceValue();

    HeatingPlaceAndCommunalAreaAdvance getUserHeatingAdvance();

}
