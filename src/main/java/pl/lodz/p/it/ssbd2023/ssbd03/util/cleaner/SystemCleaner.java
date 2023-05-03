package pl.lodz.p.it.ssbd2023.ssbd03.util.cleaner;

import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountFacade;

@Startup
@Singleton
@RunAs(Roles.ADMIN)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(TrackerInterceptor.class)
public class SystemCleaner {
    @Inject
    private AccountFacade accountFacade;

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void cleanUnconfirmedAccounts() {
        if (!accountFacade.findAllUnconfirmedAccounts().isEmpty()) {
            accountFacade.findAllUnconfirmedAccounts().forEach(account -> accountFacade.remove(account));
        }
    }
}
