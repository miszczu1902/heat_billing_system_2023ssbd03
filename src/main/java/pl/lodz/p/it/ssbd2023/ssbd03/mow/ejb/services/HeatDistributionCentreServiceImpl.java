package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.*;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class HeatDistributionCentreServiceImpl extends AbstractService implements HeatDistributionCentreService, SessionSynchronization {
    @Inject
    private HeatDistributionCentrePayoffFacade heatDistributionCentrePayoffFacade;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private HotWaterEntryFacade hotWaterEntryFacade;

    @Inject
    private PlaceFacade placeFacade;

    @Inject
    private MonthPayoffFacade monthPayoffFacade;

    @Inject
    private MessageSigner messageSigner;

    @Inject
    private HeatDistributionCentreFacade heatDistributionCentreFacade;

    @Inject
    private HeatingPlaceAndCommunalAreaAdvanceFacade heatingPlaceAndCommunalAreaAdvanceFacade;

    @Inject
    private AccessLevelFacade accessLevelFacade;

    @Override
    @RolesAllowed({Roles.MANAGER})
    public Void getHeatDistributionCentreParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void insertAdvanceChangeFactor(BigDecimal heatingAreaFactorValue, Long buildingId, String etag, Long version) {
        HeatingPlaceAndCommunalAreaAdvance actualAdvanceChangeFactor = getActualAdvanceChangeFactor(buildingId);
        if (!etag.equals(messageSigner.sign(actualAdvanceChangeFactor))) {
            throw AppException.createVerifierException();
        }
        if (!actualAdvanceChangeFactor.getVersion().equals(version)) {
            throw AppException.createOptimisticLockAppException();
        }

        LocalDate date = LocalDate.now();
        if (date.getDayOfMonth() != 1) { // sprawdzamy czy mam pierszy dzień w danym miesiącu na nowy kwartał
            throw AppException.createAdvanceChangeFactorNotModifiedException();
        } else date = date.minusMonths(3); //sprawdzamy czy w kwartale zmodyfikowano wspolczynnik

        if (heatingPlaceAndCommunalAreaAdvanceFacade.checkIfAdvanceChangeFactorNotModified(buildingId, date)) {
            final List<Place> places = placeFacade.findPlacesByBuildingId(buildingId).stream()
                    .filter(Place::getCentralHeatingConnection)
                    .toList();
            places.forEach(place -> heatingPlaceAndCommunalAreaAdvanceFacade.create(
                    new HeatingPlaceAndCommunalAreaAdvance(LocalDate.now(), place,
                            new BigDecimal(0), new BigDecimal(0), heatingAreaFactorValue)));

            //TODO - tu trzeba dodac wywolanie metody liczaczej zaliczke z mowSchedulera
        } else {
            throw AppException.createAdvanceChangeFactorWasInsertedException();
        }
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public HeatingPlaceAndCommunalAreaAdvance getActualAdvanceChangeFactor(Long buildingId) {
        return heatingPlaceAndCommunalAreaAdvanceFacade.findTheNewestAdvanceChangeFactor(buildingId);
    }

    @Override
    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public void insertConsumption(BigDecimal consumptionValue, Long placeId) {
        if (hotWaterEntryFacade.getEntryWithCheckingIfHotWaterEntryCouldBeInsertedOrOverwritten(placeId, false) == null) {
            final String username = securityContext.getCallerPrincipal().getName();
            final Place place = placeFacade.findPlaceById(placeId);

            if (place == null || !place.getHotWaterConnection()) {
                throw AppException.createHotWaterEntryCouldNotBeInsertedException();
            }

            HotWaterEntry hotWaterEntry = new HotWaterEntry(LocalDate.now(), consumptionValue, place);
            if (securityContext.isCallerInRole(Roles.MANAGER)) {
                final Manager manager = accessLevelFacade.findManagerByUsername(username);
                hotWaterEntry.setManager(manager);
            }
            hotWaterEntryFacade.create(hotWaterEntry);
        } else {
            throw AppException.createHotWaterEntryCouldNotBeInsertedException();
        }
    }

    @Override
    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public void modifyConsumption(BigDecimal consumptionValue, Long placeId, Long version, String etag) {
        HotWaterEntry hotWaterEntry = hotWaterEntryFacade.getEntryWithCheckingIfHotWaterEntryCouldBeInsertedOrOverwritten(placeId, true);
        if (hotWaterEntry != null) {
            if (!etag.equals(messageSigner.sign(hotWaterEntry))) {
                throw AppException.createVerifierException();
            }
            if (!hotWaterEntry.getVersion().equals(version)) {
                throw AppException.createOptimisticLockAppException();
            }
            if (securityContext.isCallerInRole(Roles.MANAGER)) {
                final String username = securityContext.getCallerPrincipal().getName();
                hotWaterEntry.setManager(accessLevelFacade.findManagerByUsername(username));
            }
            hotWaterEntry.setEntryValue(consumptionValue);
            hotWaterEntry.setDate(LocalDate.now());
            hotWaterEntryFacade.edit(hotWaterEntry);
        } else {
            throw AppException.createHotWaterEntryCouldNotBeModifiedException();
        }
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void addConsumptionFromInvoice(BigDecimal consumption, BigDecimal consumptionCost, BigDecimal heatingAreaFactor, Manager manager) {
        if (!heatDistributionCentrePayoffFacade.checkIfRecordForThisMonthNotExists()) {
            throw AppException.createConsumptionAddException();
        }
        final List<HeatDistributionCentre> heatDistributionCentre = heatDistributionCentreFacade.getListOfHeatDistributionCentre();
        if (heatDistributionCentre.isEmpty()) {
            throw AppException.createNoHeatDistributionCentreException();
        }
        final HeatDistributionCentrePayoff heatDistributionCentrePayoff = new HeatDistributionCentrePayoff(consumption, consumptionCost, LocalDate.now(), heatingAreaFactor, manager, heatDistributionCentre.get(0));
        heatDistributionCentrePayoffFacade.create(heatDistributionCentrePayoff);

        final List<Place> places = placeFacade.findAllPlaces();
        final BigDecimal waterHeatingUnitCost = heatDistributionCentrePayoff.getConsumptionCost().multiply(new BigDecimal(1)
                .subtract(heatDistributionCentrePayoff.getHeatingAreaFactor())).divide(heatDistributionCentrePayoff.getConsumption(), 2, RoundingMode.CEILING);
        final BigDecimal centralHeatingUnitCost = heatDistributionCentrePayoff.getConsumptionCost().multiply(heatDistributionCentrePayoff.getHeatingAreaFactor())
                .divide(heatDistributionCentrePayoff.getConsumption(), 2, RoundingMode.CEILING);

        for (Place place : places) {
            final List<HotWaterEntry> hotWaterEntries = hotWaterEntryFacade.getListOfHotWaterEntriesForPlace(place.getId());
            final MonthPayoff monthPayoff;

            if (hotWaterEntries.size() == 1) {
                final BigDecimal hotWaterConsumption = hotWaterEntries.get(0).getEntryValue();
                monthPayoff = new MonthPayoff(LocalDate.now(), waterHeatingUnitCost, centralHeatingUnitCost, hotWaterConsumption, place, place.getOwner());
            } else if (hotWaterEntries.isEmpty()) {
                monthPayoff = new MonthPayoff(LocalDate.now(), waterHeatingUnitCost, centralHeatingUnitCost, place.getPredictedHotWaterConsumption(), place, place.getOwner());
            } else {
                final BigDecimal hotWaterConsumption = hotWaterEntries.get(0).getEntryValue().subtract(hotWaterEntries.get(1).getEntryValue());
                monthPayoff = new MonthPayoff(LocalDate.now(), waterHeatingUnitCost, centralHeatingUnitCost, hotWaterConsumption, place, place.getOwner());
            }

            monthPayoffFacade.create(monthPayoff);
        }
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public HeatDistributionCentre getHeatDistributionCentre(Long id) {
        return heatDistributionCentreFacade.getHeatDistributionCentre(id);
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public HotWaterEntry getHotWaterEntry(Long hotWaterEntryId) {
        return hotWaterEntryFacade.find(hotWaterEntryId);
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public List<HotWaterEntry> getHotWaterEntriesForPlace(Long placeId) {
        if (securityContext.isCallerInRole(Roles.OWNER)) {
            final String username = securityContext.getCallerPrincipal().getName();
            final Place place = placeFacade.findPlaceById(placeId);

            if (place != null && !place.getOwner().getAccount().getUsername().equals(username)) {
                throw AppException.createNotOwnerOfPlaceException();
            }
        }
        return hotWaterEntryFacade.getHotWaterEntriesByPlaceId(placeId);
    }
}
