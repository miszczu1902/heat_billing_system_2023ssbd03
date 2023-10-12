package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ejb.TransactionAttribute;
import jakarta.transaction.Transactional;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.core.Context;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.*;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@ApplicationScoped@Transactional(value = Transactional.TxType.REQUIRES_NEW, rollbackOn = AppException.class)
public class HeatDistributionCentreServiceImpl extends AbstractService implements HeatDistributionCentreService {
    @Inject HeatDistributionCentrePayoffFacade heatDistributionCentrePayoffFacade;

    @Context SecurityContext securityContext;

    @Inject HotWaterEntryFacade hotWaterEntryFacade;

    @Inject PlaceFacade placeFacade;

    @Inject BuildingFacade buildingFacade;

    @Inject MonthPayoffFacade monthPayoffFacade;

    @Inject MessageSigner messageSigner;

    @Inject HeatDistributionCentreFacade heatDistributionCentreFacade;

    @Inject HeatingPlaceAndCommunalAreaAdvanceFacade heatingPlaceAndCommunalAreaAdvanceFacade;

    @Inject AccessLevelFacade accessLevelFacade;

    @Inject ManagerFacade managerFacade;

    @Override
    @RolesAllowed({Roles.MANAGER})
    public List<HeatDistributionCentrePayoff> getHeatDistributionCentreParameters() {
        return heatDistributionCentrePayoffFacade.findAllHeatDistributionCentrePayoff();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void insertAdvanceChangeFactor(BigDecimal heatingAreaFactorValue, Long buildingId, String etag, Long version) {
        final HeatingPlaceAndCommunalAreaAdvance actualAdvanceChangeFactor = getActualAdvanceChangeFactor(buildingId);

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
    @RolesAllowed(Roles.MANAGER)
    public void insertConsumption(BigDecimal consumptionValue, Long placeId) {
        if (hotWaterEntryFacade.getEntryWithCheckingIfHotWaterEntryCouldBeInsertedOrOverwritten(placeId, false) == null) {
            final String username = securityContext.getCallerPrincipal().getName();
            final Place place = placeFacade.findPlaceById(placeId);

            if (place == null || !place.getHotWaterConnection()) {
                throw AppException.createHotWaterEntryCouldNotBeInsertedException();
            }

            final List<HotWaterEntry> hotWaterEntries = getHotWaterEntriesForPlaceWithoutActualEntry(placeId);
            if (!hotWaterEntries.isEmpty()) {
                final HotWaterEntry newestHotWaterEntry = hotWaterEntries.get(0);
                if (consumptionValue.compareTo(newestHotWaterEntry.getEntryValue()) < 0) {
                    throw AppException.createHotWaterEntryCouldNotBeInsertedException();
                }
            }

            HotWaterEntry hotWaterEntry = new HotWaterEntry(LocalDate.now(), consumptionValue, place);
            accessLevelFacade.findManagerByUsername((username)).ifPresent(manager -> {
                if (place.getOwner().getAccount().getUsername().equals(username)) {
                    throw AppException.createManagerWhoIsOwnerOfPlaceCouldNotInsertHotWaterEntryException();
                }
                hotWaterEntry.setManager(manager);
            });

            hotWaterEntryFacade.create(hotWaterEntry);
        } else {
            throw AppException.createHotWaterEntryCouldNotBeInsertedException();
        }
    }

    @Override
    @RolesAllowed(Roles.OWNER)
    public void insertConsumptionByOwner(BigDecimal consumptionValue, Long placeId) {
        if (hotWaterEntryFacade.getEntryWithCheckingIfHotWaterEntryCouldBeInsertedOrOverwritten(placeId, false) == null) {
            final String username = securityContext.getCallerPrincipal().getName();
            final Place place = placeFacade.findPlaceById(placeId);

            if (place == null || !place.getHotWaterConnection()) {
                throw AppException.createHotWaterEntryCouldNotBeInsertedException();
            }

            if (!place.getOwner().getAccount().getUsername().equals(username)) {
                throw AppException.createNotOwnerOfPlaceException();
            }

            final List<HotWaterEntry> hotWaterEntries = getHotWaterEntriesForPlaceWithoutActualEntry(placeId);
            if (!hotWaterEntries.isEmpty()) {
                final HotWaterEntry newestHotWaterEntry = hotWaterEntries.get(0);
                if (consumptionValue.compareTo(newestHotWaterEntry.getEntryValue()) < 0) {
                    throw AppException.createHotWaterEntryCouldNotBeInsertedException();
                }
            }

            HotWaterEntry hotWaterEntry = new HotWaterEntry(LocalDate.now(), consumptionValue, place);
            hotWaterEntryFacade.create(hotWaterEntry);
        } else {
            throw AppException.createHotWaterEntryCouldNotBeInsertedException();
        }
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public void modifyConsumption(BigDecimal consumptionValue, Long placeId, Long version, String etag) {
        HotWaterEntry hotWaterEntry = hotWaterEntryFacade.getEntryWithCheckingIfHotWaterEntryCouldBeInsertedOrOverwritten(placeId, true);
        if (hotWaterEntry != null) {
            if (!etag.equals(messageSigner.sign(hotWaterEntry))) {
                throw AppException.createVerifierException();
            }
            if (!hotWaterEntry.getVersion().equals(version)) {
                throw AppException.createOptimisticLockAppException();
            }

            final HotWaterEntry newestHotWaterEntry = hotWaterEntryFacade.getHotWaterEntriesByPlaceId(placeId).get(0);
            if (consumptionValue.compareTo(newestHotWaterEntry.getEntryValue()) < 0) {
                throw AppException.createHotWaterEntryCouldNotBeInsertedException();
            }

            final String username = securityContext.getCallerPrincipal().getName();
            accessLevelFacade.findManagerByUsername((username)).ifPresent(manager -> {
                if (hotWaterEntry.getPlace().getOwner().getAccount().getUsername().equals(username)) {
                    throw AppException.createManagerWhoIsOwnerOfPlaceCouldNotInsertHotWaterEntryException();
                }
                hotWaterEntry.setManager(manager);
            });
            hotWaterEntry.setEntryValue(consumptionValue);
            hotWaterEntry.setDate(LocalDate.now());
            hotWaterEntryFacade.edit(hotWaterEntry);
        } else {
            throw AppException.createHotWaterEntryCouldNotBeModifiedException();
        }
    }

    @Override
    @RolesAllowed(Roles.OWNER)
    public void modifyConsumptionByOwner(BigDecimal consumptionValue, Long placeId, Long version, String etag) {
        HotWaterEntry hotWaterEntry = hotWaterEntryFacade.getEntryWithCheckingIfHotWaterEntryCouldBeInsertedOrOverwritten(placeId, true);
        if (hotWaterEntry != null) {
            if (!etag.equals(messageSigner.sign(hotWaterEntry))) {
                throw AppException.createVerifierException();
            }
            if (!hotWaterEntry.getVersion().equals(version)) {
                throw AppException.createOptimisticLockAppException();
            }

            final HotWaterEntry newestHotWaterEntry = hotWaterEntryFacade.getHotWaterEntriesByPlaceId(placeId).get(0);
            if (consumptionValue.compareTo(newestHotWaterEntry.getEntryValue()) < 0) {
                throw AppException.createHotWaterEntryCouldNotBeInsertedException();
            }

            final String username = securityContext.getCallerPrincipal().getName();
            if (!placeFacade.findPlaceById(placeId).getOwner().getAccount().getUsername().equals(username)) {
                throw AppException.createNotOwnerOfPlaceException();
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
    public void addConsumptionFromInvoice(BigDecimal consumption, BigDecimal consumptionCost, BigDecimal heatingAreaFactor) {
        final Manager manager = managerFacade.findManagerByUsername(securityContext.getCallerPrincipal().getName());

        if (!heatDistributionCentrePayoffFacade.checkIfRecordForThisMonthNotExists()) {
            throw AppException.createConsumptionAddException();
        }
        final List<HeatDistributionCentre> heatDistributionCentre = heatDistributionCentreFacade.getListOfHeatDistributionCentre();
        if (heatDistributionCentre.isEmpty()) {
            throw AppException.createNoHeatDistributionCentreException();
        }
        final HeatDistributionCentrePayoff heatDistributionCentrePayoff = new HeatDistributionCentrePayoff(consumption, consumptionCost, LocalDate.now(), heatingAreaFactor, manager, heatDistributionCentre.get(0));
        heatDistributionCentrePayoffFacade.create(heatDistributionCentrePayoff);

        final List<Place> places = placeFacade.findAllPlacesAddedBeforeDate(LocalDateTime.now(TIME_ZONE).minusMonths(1).with(TemporalAdjusters
                .firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS));
        final List<Building> buildings = buildingFacade.findAllBuildings();

        if (buildings.isEmpty()) {
            throw AppException.noBuildingFoundException();
        }

        if (places.isEmpty()) {
            throw AppException.noPlaceFoundException();
        }

        BigDecimal totalBuildingsArea = BigDecimal.ZERO;
        for (Building building : buildings) {
            totalBuildingsArea = totalBuildingsArea.add(building.getTotalArea());
        }

        if (totalBuildingsArea.compareTo(BigDecimal.ZERO) == 0) {
            throw AppException.totalAreaIsZeroException();
        }

        BigDecimal totalHotWaterUsage = BigDecimal.ZERO;

        for (Place place : places) {
            final List<HotWaterEntry> hotWaterEntries = hotWaterEntryFacade.getListOfHotWaterEntriesForPlace(place.getId());

            if (hotWaterEntries.size() == 1) {
                totalHotWaterUsage = totalHotWaterUsage.add(hotWaterEntries.get(0).getEntryValue());
            } else if (hotWaterEntries.size() > 1) {
                totalHotWaterUsage = totalHotWaterUsage.add(hotWaterEntries.get(0).getEntryValue().subtract(hotWaterEntries.get(1).getEntryValue()));
            }
        }

        if (totalHotWaterUsage.compareTo(BigDecimal.ZERO) == 0) {
            throw AppException.totalHotWaterUsageIsZeroException();
        }

        final BigDecimal centralHeatingUnitCost = heatDistributionCentrePayoff.getConsumptionCost().multiply(heatDistributionCentrePayoff.getHeatingAreaFactor()).divide(totalBuildingsArea, 2, RoundingMode.CEILING);
        final BigDecimal waterHeatingUnitCost = heatDistributionCentrePayoff.getConsumptionCost().multiply(BigDecimal.ONE
                .subtract(heatDistributionCentrePayoff.getHeatingAreaFactor())).divide(totalHotWaterUsage, 2, RoundingMode.CEILING);

        for (Place place : places) {
            final List<HotWaterEntry> hotWaterEntries = hotWaterEntryFacade.getListOfHotWaterEntriesForPlace(place.getId());
            final MonthPayoff monthPayoff;

            if (hotWaterEntries.isEmpty()) {
                monthPayoff = new MonthPayoff(LocalDate.now(), waterHeatingUnitCost, centralHeatingUnitCost, BigDecimal.ZERO, place, place.getOwner());

            } else if (hotWaterEntries.size() == 1) {
                monthPayoff = new MonthPayoff(LocalDate.now(), waterHeatingUnitCost, centralHeatingUnitCost, hotWaterEntries.get(0).getEntryValue(), place, place.getOwner());
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
    @RolesAllowed(Roles.OWNER)
    public HotWaterEntry getHotWaterEntryForOwner(Long hotWaterEntryId) {
        final HotWaterEntry hotWaterEntry = hotWaterEntryFacade.find(hotWaterEntryId);
        final String username = securityContext.getCallerPrincipal().getName();
        Place place = hotWaterEntry.getPlace();
        if (place != null && !place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createNotOwnerOfPlaceException();
        }

        return hotWaterEntry;
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public HotWaterEntry getHotWaterEntry(Long hotWaterEntryId) {
        return hotWaterEntryFacade.find(hotWaterEntryId);
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public List<HotWaterEntry> getHotWaterEntriesForPlace(Long placeId) {
        return hotWaterEntryFacade.getHotWaterEntriesByPlaceId(placeId);
    }

    @RolesAllowed(Roles.OWNER)
    public List<HotWaterEntry> getHotWaterEntriesForPlaceForOwner(Long placeId) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Place place = placeFacade.findPlaceById(placeId);

        if (place != null && !place.getOwner().getAccount().getUsername().equals(username)) {
            throw AppException.createNotOwnerOfPlaceException();
        }
        return hotWaterEntryFacade.getHotWaterEntriesByPlaceId(placeId);
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
 List<HotWaterEntry> getHotWaterEntriesForPlaceWithoutActualEntry(Long placeId) {
        final List<HotWaterEntry> hotWaterEntries = hotWaterEntryFacade.getHotWaterEntriesByPlaceId(placeId);
        final LocalDate now = LocalDate.now();
        hotWaterEntries.removeIf(entry -> entry.getDate().getYear() == now.getYear() && entry.getDate().getMonthValue() == now.getMonthValue());
        return hotWaterEntries;
    }
}