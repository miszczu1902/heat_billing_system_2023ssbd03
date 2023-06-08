package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.ejb.Local;
import jakarta.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Local
public interface PlaceService extends CommonManagerLocalInterface {

    void modifyPlaceOwner();

    void modifyPlace();

    void enterHotWaterConsumption(String placeId, LocalDate date, BigDecimal value);

    void enterPredictedHotWaterConsumption(String placeId, BigDecimal consumption, String etag, Long version,
                                           String username, boolean isOwner);

    Place getPlace(String placeId);

    List<Place> getSelfAllPlaces(int pageNumber, int pageSize);
}
