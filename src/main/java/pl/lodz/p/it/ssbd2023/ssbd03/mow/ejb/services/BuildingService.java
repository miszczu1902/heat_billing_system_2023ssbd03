package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import java.math.BigDecimal;
import java.util.List;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

@Local
public interface BuildingService extends CommonManagerLocalInterface {

    List<Place> getAllPlaces(String buildingId, int pageNumber, int pageSize);

    Building getBuilding(String buildingId);

    List<Building> getAllBuildings(int pageNumber, int pageSize);

    void addBuilding(Building building);

    void addPlaceToBuilding(BigDecimal area, Boolean hotWaterConnection, BigDecimal predictedHotWaterConsumption,
                            Long buildingId, Long ownerId, String etag, Long version);

    List<Account> getListOfOwners();
}
