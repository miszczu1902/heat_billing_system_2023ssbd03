package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

public interface AdvanceService extends CommonManagerLocalInterface {
    void calculateHotWaterAdvance();
    void calculateHeatingPlaceAndCommunalAreaAdvance();
}