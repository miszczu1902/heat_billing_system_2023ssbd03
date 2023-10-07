package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;

public interface AdvanceService extends CommonManagerLocalInterface {
    void calculateHotWaterAdvance();

    void calculateHeatingPlaceAndCommunalAreaAdvance();
}