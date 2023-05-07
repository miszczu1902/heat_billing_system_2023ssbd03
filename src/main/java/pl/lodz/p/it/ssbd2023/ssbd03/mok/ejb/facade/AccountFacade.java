package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;

import java.util.List;

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

    public Account findByUsername(String username) {
        TypedQuery<Account> tq = em.createNamedQuery("Account.findByUsername", Account.class);
        tq.setParameter("username", username);
        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

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
}

