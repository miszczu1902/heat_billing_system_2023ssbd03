package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
public class AccountConfirmationTokenFacade extends AbstractFacade<AccountConfirmationToken> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    public AccountConfirmationTokenFacade() {
        super(AccountConfirmationToken.class);
    }

    @RolesAllowed({Roles.GUEST, Roles.ADMIN})
    public List<AccountConfirmationToken> findAllUnconfirmedAccounts() {
        TypedQuery<AccountConfirmationToken> query = em.createNamedQuery("AccountConfirmationToken.findAllUnconfirmedAccounts", AccountConfirmationToken.class);
        query.setParameter("date", LocalDateTime.now().minusDays(1));
        return Optional.of(query.getResultList()).orElse(Collections.emptyList());
    }

    @RolesAllowed({Roles.GUEST, Roles.ADMIN})
    public AccountConfirmationToken getActivationTokenByTokenValue(String tokenValue) {
        TypedQuery<AccountConfirmationToken> query = em.createNamedQuery("AccountConfirmationToken.getActivationTokenByTokenValue", AccountConfirmationToken.class);
        query.setParameter("tokenValue", tokenValue);
        return query.getSingleResult();
    }
}
