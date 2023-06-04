package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import java.util.List;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd03.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

@Local
public interface BuildingService extends CommonManagerLocalInterface {

    List<Place> getAllPlaces(String buildingId, int pageNumber, int pageSize);

    Building getBuilding(String buildingId);

    List<Building> getAllBuildings(int pageNumber, int pageSize);

    void modifyBuilding(String buildingId);

    void addBuilding(Building building);

    void addPlaceToBuilding();
}
