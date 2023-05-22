package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public void create(Account entity) {
        super.create(entity);
    }

    @Override
    @PermitAll
    public void edit(Account entity) {
        super.edit(entity);
    }

    @RolesAllowed({Roles.GUEST, Roles.OWNER, Roles.ADMIN, Roles.MANAGER})
    public Account findByUsername(String username) {
        TypedQuery<Account> tq = em.createNamedQuery("Account.findByUsername", Account.class);
        tq.setParameter("username", username);
        return tq.getSingleResult();
    }

    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public List<Account> getListOfAccountsWithFilterParams(String sortBy, int pageNumber, int pageSize, Boolean isEnable) {
        TypedQuery<Account> tq;
        if (isEnable != null) {
            if (sortBy.equals("email"))
                tq = em.createNamedQuery("Account.getListOfAccountsByEmailAndEnableStatus", Account.class);
            else tq = em.createNamedQuery("Account.getListOfAccountsByUsernameAndEnableStatus", Account.class);
            tq.setParameter("isEnable", isEnable);
        } else {
            if (sortBy.equals("email")) tq = em.createNamedQuery("Account.getListOfAccountsByEmail", Account.class);
            else tq = em.createNamedQuery("Account.getListOfAccountsByUsername", Account.class);
        }

        if (pageNumber != 0) {
            tq.setFirstResult((pageNumber - 1) * pageSize);
            tq.setMaxResults(pageSize);
        }

        return tq.getResultList();
    }

    @PermitAll
    public List<Account> findAllBlockedAccounts() {
        TypedQuery<Account> query = em.createNamedQuery("Account.findAllBlockedAccounts", Account.class);
        query.setParameter("date", LocalDateTime.now(TIME_ZONE).minusDays(1));
        return Optional.of(query.getResultList()).orElse(Collections.emptyList());
    }

    @Override
    @PermitAll
    public void remove(Account entity) {
        super.remove(entity);
    }

    @PermitAll
    public boolean checkIfAnAccountExistsByEmail(String email) {
        TypedQuery<Account> tq = em.createNamedQuery("Account.findByEmail", Account.class);
        tq.setParameter("email", email);
        try {
            tq.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}