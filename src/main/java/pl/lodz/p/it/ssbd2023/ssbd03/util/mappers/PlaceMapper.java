package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.PlaceDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

public class PlaceMapper {
    public static PlaceDTO createPlaceToPlaceDTO(Place place) {
        return new PlaceDTO(
                place.getId(),
                place.getVersion(),
                place.getCreatedBy() == null ?
                        null :
                        place.getCreatedBy().getUsername(),
                place.getPlaceNumber(),
                place.getArea(),
                place.getHotWaterConnection(),
                place.getCentralHeatingConnection(),
                place.getPredictedHotWaterConsumption() == null ?
                        null :
                        place.getPredictedHotWaterConsumption(),
                place.getOwner().getAccount().getPersonalData().getFirstName(),
                place.getOwner().getAccount().getPersonalData().getSurname()
        );
    }
}