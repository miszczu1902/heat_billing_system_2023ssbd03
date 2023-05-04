package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
public class PersonalDataFacade extends AbstractFacade<PersonalData> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    public PersonalDataFacade() {
        super(PersonalData.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    public PersonalData findByLogin(String login) {
        TypedQuery<PersonalData> tq = em.createNamedQuery("PersonalData.findByLogin", PersonalData.class);
        tq.setParameter("login", login);
        return tq.getSingleResult();
    }
}
