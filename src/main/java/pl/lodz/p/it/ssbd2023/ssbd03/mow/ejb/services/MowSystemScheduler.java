package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AnnualBalance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.BalanceFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.PlaceFacade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@Startup
@Singleton
@RunAs(Roles.MANAGER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(TrackerInterceptor.class)
public class MowSystemScheduler {
    @Inject
    private BalanceFacade balanceFacade;

    @Inject
    private PlaceFacade placeFacade;

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void updateYearReport() {
        throw new UnsupportedOperationException();
    }

    @Schedule(month = "1", dayOfMonth = "1", timezone = "Europe/Warsaw", persistent = false) //pierwszy stycznia o północy
    private void createYearReport() {
        final List<Place> places = placeFacade.findAllPlaces();
        final Short year = (short) LocalDateTime.now(TIME_ZONE).getYear();
        final BigDecimal bigDecimal = new BigDecimal(0);
        for (Place place : places) {
            final AnnualBalance annualBalance = new AnnualBalance(year, bigDecimal, bigDecimal, bigDecimal, bigDecimal,
                    bigDecimal, bigDecimal, place);
            balanceFacade.create(annualBalance);
        }
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