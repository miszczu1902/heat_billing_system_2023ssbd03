package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccessLevelMapping;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Admin;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessLevelMappingFacade extends AbstractFacade<AccessLevelMapping> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    public AccessLevelMappingFacade() {
        super(AccessLevelMapping.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    public void create(AccessLevelMapping entity) {
        if (entity instanceof Owner) {
            super.create((Owner) entity);
        } else if (entity instanceof Manager) {
            super.create((Manager) entity);
        } else if (entity instanceof Admin) {
            super.create((Admin) entity);
        } else {
            super.create(entity);
        }

    }

}
