package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.PlaceDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.PlaceInfoDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.PlacesListDTO;
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

    public static PlaceInfoDTO createPlaceToPlaceInfoDTO(Place place) {
        return new PlaceInfoDTO(
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
                place.getOwner().getAccount().getPersonalData().getSurname(),
                place.getBuilding().getAddress().getStreet(),
                place.getBuilding().getAddress().getBuildingNumber(),
                place.getBuilding().getAddress().getCity(),
                place.getBuilding().getAddress().getPostalCode()
        );
    }

    public static PlacesListDTO createPlaceToPlacesListDTO(Place place) {
        return new PlacesListDTO(
                place.getId(),
                place.getVersion(),
                place.getPlaceNumber(),
                place.getArea(),
                place.getHotWaterConnection(),
                place.getCentralHeatingConnection(),
                place.getPredictedHotWaterConsumption() == null ?
                        null :
                        place.getPredictedHotWaterConsumption(),
                place.getBuilding().getAddress().getStreet(),
                place.getBuilding().getAddress().getBuildingNumber(),
                place.getBuilding().getAddress().getCity(),
                place.getBuilding().getAddress().getPostalCode()
        );
    }
}