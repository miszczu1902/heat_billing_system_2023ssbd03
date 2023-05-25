package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Local
public interface PlaceService extends CommonManagerLocalInterface {

    void modifyPlaceOwner();

    void modifyPlace();

    void enterHotWaterConsumption(String placeId, LocalDate date, BigDecimal value);

    void enterPredictedHotWaterConsumption(String placeId, BigDecimal consumption);

    Place getPlace(String placeId);

    List<Place> getOwnerAllPlaces(String ownerUsername);

    List<Place> getSelfAllPlaces();
}
