package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateBuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.BuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Address;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;

import java.math.BigDecimal;
import java.util.ArrayList;

public class BuildingMapper {

    public static BuildingDTO createBuildingToBuildingDTO(Building building) {
        return new BuildingDTO(
                building.getId(),
                building.getVersion(),
                building.getCreatedBy() == null ?
                    null:
                    building.getCreatedBy().getUsername(),
                building.getTotalArea(),
                building.getCommunalAreaAggregate(),
                building.getAddress().getStreet(),
                building.getAddress().getBuildingNumber(),
                building.getAddress().getCity(),
                building.getAddress().getPostalCode()
        );
    }

    public static Building createBuilding(CreateBuildingDTO createBuildingDTO) {
        return new Building(
                new BigDecimal(createBuildingDTO.getTotalArea()),
                new BigDecimal(createBuildingDTO.getCommunalAreaAggregate()),
                new Address(createBuildingDTO.getStreet(),
                        createBuildingDTO.getBuildingNumber(),
                        createBuildingDTO.getCity(),
                        createBuildingDTO.getPostalCode()),
                new ArrayList<>(),
                null
        );
    }
}
