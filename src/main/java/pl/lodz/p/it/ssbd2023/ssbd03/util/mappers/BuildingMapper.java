package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateBuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.BuildingDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Address;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.HeatDistributionCentreService;

import java.util.ArrayList;

@ApplicationScoped
public class BuildingMapper {

    @Inject
    HeatDistributionCentreService service;

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

    public Building createBuilding(CreateBuildingDTO createBuildingDTO) {
        return new Building(
                createBuildingDTO.getTotalArea(),
                createBuildingDTO.getTotalArea(),
                new Address(createBuildingDTO.getStreet(),
                        createBuildingDTO.getBuildingNumber(),
                        createBuildingDTO.getCity(),
                        createBuildingDTO.getPostalCode()),
                new ArrayList<>(),
                service.getHeatDistributionCentre(Long.valueOf("0")));
    }
}
