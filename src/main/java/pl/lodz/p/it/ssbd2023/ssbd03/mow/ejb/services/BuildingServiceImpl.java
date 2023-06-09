package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import jakarta.security.enterprise.SecurityContext;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.*;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Internationalization;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BuildingServiceImpl extends AbstractService implements BuildingService, SessionSynchronization {
    @Inject
    private BuildingFacade buildingFacade;

    @Inject
    private PlaceFacade placeFacade;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private BalanceFacade balanceFacade;

    @Inject
    private OwnerFacade ownerFacade;

    @Inject
    private Internationalization internationalization;

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private MessageSigner messageSigner;

    @Inject
    private SecurityContext securityContext;

    @Override
    @RolesAllowed(Roles.MANAGER)
    public List<Place> getAllPlaces(String buildingId, int pageNumber, int pageSize) {
        final Long id = Long.valueOf(buildingId);
        buildingFacade.findById(id);
        return placeFacade.findByBuildingId(id, pageNumber, pageSize);
    }
    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Building getBuilding(String buildingId) {
        final Long id = Long.valueOf(buildingId);
        return buildingFacade.findById(id);
    }
    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void modifyBuilding(String buildingId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void addBuilding(Building building) {
        buildingFacade.create(building);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void addPlaceToBuilding(BigDecimal area, Boolean hotWaterConnection, Boolean centralHeatingConnection,
                                   BigDecimal predictedHotWaterConsumption, Long buildingId, Long ownerId, String etag, Long version) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByUsername(username);

        final Owner owner = ownerFacade.findById(ownerId);

        if (owner.getId().equals(account.getId())) {
            throw AppException.addingPlaceToTheSameAccountException();
        }

        final Building building = buildingFacade.findById(buildingId);

        if (!etag.equals(messageSigner.sign(building))) {
            throw AppException.createVerifierException();
        }
        if (!Objects.equals(version, building.getVersion())) {
            throw AppException.createOptimisticLockAppException();
        }

        final BigDecimal sum = building.getPlaces().stream()
                .map(Place::getArea)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal newCommunalAreaAggreagate = building.getTotalArea().subtract(sum).subtract(area);
        final int comparisonResult = newCommunalAreaAggreagate.compareTo(building.getTotalArea().multiply(BigDecimal.valueOf(0.1)));
        if (comparisonResult < 0) {
            throw AppException.lackOfSpaceInTheBuildingException();
        }

        final Short placeNumber = (short) (building.getPlaces().size() + 1);
        final Place place = new Place(placeNumber, area, hotWaterConnection, centralHeatingConnection, predictedHotWaterConsumption, building, owner);
        placeFacade.create(place);

        building.setCommunalAreaAggregate(newCommunalAreaAggreagate);
        building.getPlaces().add(place);
        buildingFacade.edit(building);

        final BigDecimal bigDecimal = new BigDecimal(0);
        final AnnualBalance annualBalance = new AnnualBalance((short) LocalDateTime.now().getYear(), bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, bigDecimal, place);
        balanceFacade.create(annualBalance);
    }
    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public List<Building> getAllBuildings(int pageNumber, int pageSize) {
        return buildingFacade.getListOfBuildingsWithPaging(pageNumber, pageSize);
    }

}
