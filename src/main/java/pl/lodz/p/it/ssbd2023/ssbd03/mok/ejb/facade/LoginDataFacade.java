package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.LoginData;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
public class LoginDataFacade extends AbstractFacade<LoginData> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    public LoginDataFacade() {
        super(LoginData.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    public LoginData findById(Account account) {
        TypedQuery<LoginData> tq = em.createNamedQuery("LoginData.findById", LoginData.class);
        tq.setParameter("id", account);
        try {
            return tq.getSingleResult();
        } catch (PersistenceException pe) {
            if (pe instanceof NoResultException) {
                throw AppException.createNoResultException(pe.getCause());
            }
            throw AppException.createDatabaseException();
        }
    }
}
