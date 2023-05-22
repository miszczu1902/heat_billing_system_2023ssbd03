package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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