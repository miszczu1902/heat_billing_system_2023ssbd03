package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccountConfirmationToken;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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

    @PermitAll
    public List<AccountConfirmationToken> findAllUnconfirmedAccounts() {
        TypedQuery<AccountConfirmationToken> query = em.createNamedQuery("AccountConfirmationToken.findAllUnconfirmedAccounts", AccountConfirmationToken.class);
        query.setParameter("date",
                LocalDateTime.now(TIME_ZONE).minusDays(1));
        return Optional.of(query.getResultList()).orElse(Collections.emptyList());
    }

    public List<AccountConfirmationToken> findAllUnconfirmedAccountsToRemind() {
        TypedQuery<AccountConfirmationToken> query = em.createNamedQuery("AccountConfirmationToken.findAllUnconfirmedAccounts", AccountConfirmationToken.class);
        query.setParameter("date",
                LocalDateTime.now(TIME_ZONE).minusHours(12));
        return Optional.of(query.getResultList()).orElse(Collections.emptyList());
    }

    @RolesAllowed({Roles.GUEST, Roles.ADMIN})
    public AccountConfirmationToken getActivationTokenByTokenValue(String tokenValue) {
        TypedQuery<AccountConfirmationToken> query = em.createNamedQuery("AccountConfirmationToken.getActivationTokenByTokenValue", AccountConfirmationToken.class);
        query.setParameter("tokenValue", tokenValue);
        return query.getSingleResult();
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public void create(AccountConfirmationToken entity) {
        super.create(entity);
    }

    @Override
    @PermitAll
    public void remove(AccountConfirmationToken entity) {
        super.remove(entity);
    }
}
