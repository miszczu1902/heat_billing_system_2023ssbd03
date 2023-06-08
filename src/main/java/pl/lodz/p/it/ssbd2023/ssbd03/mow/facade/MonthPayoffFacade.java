package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.MonthPayoff;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class MonthPayoffFacade extends AbstractFacade<MonthPayoff> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public MonthPayoffFacade() {
        super(MonthPayoff.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void edit(MonthPayoff entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void create(MonthPayoff entity) {
        super.create(entity);
    }

    @Override
    public void remove(MonthPayoff entity) {
        super.remove(entity);
    }
}