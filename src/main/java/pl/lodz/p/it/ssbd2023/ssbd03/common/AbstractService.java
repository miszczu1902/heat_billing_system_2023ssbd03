package pl.lodz.p.it.ssbd2023.ssbd03.common;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.transaction.Synchronization;
import jakarta.transaction.Transactional;
import jakarta.interceptor.Interceptors;
import jakarta.transaction.UserTransaction;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.BasicServiceExceptionInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.InterceptionBinding;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Interceptors({TrackerInterceptor.class, BasicServiceExceptionInterceptor.class})
@InterceptionBinding
public abstract class AbstractService implements Synchronization {
    @Inject
    SecurityIdentity securityIdentity;

    UserTransaction userTransaction;

    protected static final Logger LOGGER = Logger.getGlobal();

    private String transactionId;

    private boolean lastTransactionRollback;

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED, rollbackOn = AppException.class)
    public boolean isLastTransactionRollback() {
        return lastTransactionRollback;
    }

    @Override
    public void beforeCompletion() {
        transactionId = UUID.randomUUID().toString();
        LOGGER.log(Level.INFO, "Transakcja TXid={0} przed zatwierdzeniem w {1} tożsamość: {2}",
                new Object[]{transactionId, this.getClass().getName(), securityIdentity.getPrincipal().getName()});
    }

    @Override
    public void afterCompletion(int var1) {
        lastTransactionRollback = var1 == 0;
        LOGGER.log(Level.INFO, "Transakcja TXid={0} zakończona w {1} poprzez {3}, tożsamość: {2}",
                new Object[]{transactionId, this.getClass().getName(), securityIdentity.getPrincipal().getName(),
                        lastTransactionRollback ? "ZATWIERDZENIE" : "ODWOŁANIE"});
    }
}
