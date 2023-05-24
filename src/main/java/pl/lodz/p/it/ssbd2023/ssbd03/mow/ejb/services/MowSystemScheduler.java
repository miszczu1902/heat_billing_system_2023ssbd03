package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.facade.BalanceFacade;

@Startup
@Singleton
@RunAs(Roles.ADMIN)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(TrackerInterceptor.class)
public class MowSystemScheduler {
    @Inject
    private BalanceFacade balanceFacade;

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void updateYearReport() {
        throw new UnsupportedOperationException();
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