package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
public class ManagerFacade extends AbstractFacade<Manager> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    public ManagerFacade() {
        super(Manager.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    public boolean findByLicense(String license) {
        TypedQuery<Manager> tq = em.createNamedQuery("Owner.findByLicense", Manager.class);
        tq.setParameter("license", license);
        try {
            tq.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}
