package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.PersonalDataBinding;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.transaction.Transactional;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.PersonalData;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.BasicFacadeExceptionInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.PersonalDataFacadeExceptionInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;

@Boundary
@Transactional(value = Transactional.TxType.MANDATORY, rollbackOn = AppException.class)
@Interceptors({TrackerInterceptor.class, BasicFacadeExceptionInterceptor.class,
        PersonalDataFacadeExceptionInterceptor.class})
@PersonalDataBinding
public class PersonalDataFacade extends AbstractFacade<PersonalData> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    EntityManager em;

    public PersonalDataFacade() {
        super(PersonalData.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public PersonalData findByUsername(String username) {
        TypedQuery<PersonalData> tq = em.createNamedQuery("PersonalData.findByUsername", PersonalData.class);
        tq.setParameter("username", username);
        return tq.getSingleResult();
    }

    @Override
    @RolesAllowed({Roles.OWNER, Roles.ADMIN, Roles.MANAGER})
    public PersonalData find(Object id) {
        return super.find(id);
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public void edit(PersonalData entity) {
        super.edit(entity);
    }
}