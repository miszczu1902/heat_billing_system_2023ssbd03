package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
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
        try {
            super.create(entity);
        } catch (PersistenceException pe) {
            if (pe.getCause() instanceof ConstraintViolationException) {
                throw AppException.createAccountExistsException(pe.getCause());
            }

            throw AppException.createDatabaseException();
        }
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
        try {
            return tq.getSingleResult();
        } catch (PersistenceException pe) {
            if (pe instanceof NoResultException) {
                throw AppException.createNoResultException(pe.getCause());
            }

            throw AppException.createDatabaseException();
        }
    }

    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public List<Account> getListOfAccountsWithFilterParams(String sortBy, int pageNumber) {
        TypedQuery<Account> tq;
        if (sortBy.equals("email")) tq = em.createNamedQuery("Account.getListOfAccountsByEmail", Account.class);
        else tq = em.createNamedQuery("Account.getListOfAccountsByUsername", Account.class);

        if (pageNumber != 0) {
            tq.setFirstResult((pageNumber - 1) * 10);
            tq.setMaxResults(pageNumber * 10);
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