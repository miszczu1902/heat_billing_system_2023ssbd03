package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
@RunAs(Roles.MANAGER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(TrackerInterceptor.class)
public class MowSystemScheduler {
    protected static final Logger LOGGER = Logger.getGlobal();

    private int txRetries = Integer.parseInt(LoadConfig.loadPropertyFromConfig("tx.retries"));

    @Inject
    private BalanceService balanceService;

    @Schedule(dayOfMonth = "2", timezone = "Europe/Warsaw", persistent = false) //drugi dzien kazdego miesiaca o polnocy
    private void updateYearReport() {
        final short year = (short) LocalDate.now(TIME_ZONE).getYear();
        final Month month = LocalDate.now(TIME_ZONE).getMonth();
        List<AnnualBalance> annualBalanceList;
        if (Month.JANUARY.equals(month)) { //jezeli mamy styczen to aktualizujemy jeszcze raport z poprzedniego roku
            annualBalanceList = balanceFacade.getListOfAnnualBalancesForYear((short) (year - 1));
        } else {
            annualBalanceList = balanceFacade.getListOfAnnualBalancesForYear(year);
        }
        annualBalanceList.forEach(annualBalance -> {
            final Place place = annualBalance.getPlace();
            final Optional<MonthPayoff> monthPayoffForThisYearOptional = place.getMonthPayoffs().stream()
                    .filter(monthPayoff -> monthPayoff.getPayoffDate().getYear() == year
                            && monthPayoff.getPayoffDate().getMonth() == month).findFirst();

            monthPayoffForThisYearOptional.ifPresent(monthPayoffForThisYear -> {
                final BigDecimal hotWaterCost = monthPayoffForThisYear.getHotWaterConsumption()
                        .multiply(monthPayoffForThisYear.getWaterHeatingUnitCost());
                final BigDecimal placeCost = monthPayoffForThisYear.getCentralHeatingUnitCost()
                        .multiply(place.getArea());
                final BigDecimal communalAreaCost = monthPayoffForThisYear.getCentralHeatingUnitCost()
                        .multiply(place.getBuilding().getCommunalAreaAggregate());

                annualBalance.setTotalHotWaterCost(annualBalance.getTotalHotWaterAdvance().add(hotWaterCost));
                annualBalance.setTotalHeatingPlaceCost(annualBalance.getTotalHeatingPlaceCost().add(placeCost));
                annualBalance.setTotalHeatingCommunalAreaCost(annualBalance.getTotalHeatingCommunalAreaCost()
                        .add(communalAreaCost));

                balanceFacade.edit(annualBalance);
            });
        });
    }

    @Schedule(month = "1", dayOfMonth = "1", timezone = "Europe/Warsaw", persistent = false) //pierwszy stycznia o północy
    private void createYearReports() {
        int retryTXCounter = txRetries; //limit prób ponowienia transakcji
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                balanceService.createYearReports();
                rollbackTX = balanceService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return;
            } catch (Exception ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        balanceService.createYearReports();
    }

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void calculateMainPayoff() {
        throw new UnsupportedOperationException();
    }

    private void generateYearHeatReport() {
        throw new UnsupportedOperationException();
    }

    private void calculateCommunalAreaPayoff() {
        throw new UnsupportedOperationException();
    }

    private void calculatePlaceAreaPayoff() {
        throw new UnsupportedOperationException();
    }

    private void calculateHotWaterPayoff() {
        throw new UnsupportedOperationException();
    }

    private void calculateHotWaterBalance() {
        throw new UnsupportedOperationException();
    }

    private void calculateAreaBalance() {
        throw new UnsupportedOperationException();
    }

    private void calculateCommunalAreaBalance() {
        throw new UnsupportedOperationException();
    }
}