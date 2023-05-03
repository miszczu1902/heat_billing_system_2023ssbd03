package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public List<Account> findAllUnconfirmedAccounts() {
        TypedQuery<Account> query = em.createNamedQuery("Account.findAllUnconfirmedAccounts", Account.class);
        query.setParameter("date", LocalDateTime.now().minusDays(1));
        return Optional.of(query.getResultList()).orElse(Collections.emptyList());
    }

    public Account findByUsername(String username) {
        TypedQuery<Account> tq = em.createNamedQuery("Account.findByUsername", Account.class);
        tq.setParameter("username", username);
        return tq.getSingleResult();
    }

    public Owner findByPhoneNumber(String phoneNumber) {
        TypedQuery<Owner> tq = em.createNamedQuery("Owner.findByPhoneNumber", Owner.class);
        tq.setParameter("phoneNumber", phoneNumber);
        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
