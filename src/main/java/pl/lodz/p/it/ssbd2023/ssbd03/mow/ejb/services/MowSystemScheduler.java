package pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services;

import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database.OptimisticLockAppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.transactions.TransactionRollbackException;
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

    private final int txRetries = Integer.parseInt(LoadConfig.loadPropertyFromConfig("tx.retries"));

    @Inject
    private AdvanceService advanceService;

    @Inject
    private BalanceService balanceService;

    @Schedule(dayOfMonth = "2", timezone = "Europe/Warsaw", persistent = false) //drugi dzien kazdego miesiaca o polnocy
    private void updateYearReports() {
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                balanceService.updateTotalCostYearReports();
                rollbackTX = balanceService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return;
            } catch (OptimisticLockAppException | TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        balanceService.updateTotalCostYearReports();
    }

    @Schedule(month = "1", dayOfMonth = "1", timezone = "Europe/Warsaw", persistent = false) //pierwszy stycznia o północy
    private void createYearReports() {
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                balanceService.createYearReports();
                rollbackTX = balanceService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return;
            } catch (TransactionRollbackException ex) {
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

    @Schedule(dayOfMonth = "2", hour = "1", month = "1,4,7,10", timezone = "Europe/Warsaw", persistent = false)
    private void calculatePastQuarterHotWaterPayoff() {
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                advanceService.calculateHotWaterAdvance();
                rollbackTX = advanceService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return;
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        advanceService.calculateHotWaterAdvance();
    }

    @Schedule(dayOfMonth = "2", hour = "2", month = "1,4,7,10", timezone = "Europe/Warsaw", persistent = false)
    private void calculateHeatingPlaceAndCommunalAreaAdvance() {
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                advanceService.calculateHeatingPlaceAndCommunalAreaAdvance();
                rollbackTX = advanceService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return;
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        advanceService.calculateHeatingPlaceAndCommunalAreaAdvance();
    }
}