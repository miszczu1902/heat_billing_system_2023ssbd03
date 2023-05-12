package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
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

    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public PersonalData findByUsername(String username) {
        TypedQuery<PersonalData> tq = em.createNamedQuery("PersonalData.findByUsername", PersonalData.class);
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