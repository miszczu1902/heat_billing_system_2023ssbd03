package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Admin;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AdminFacade extends AbstractFacade<Admin> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    public AdminFacade() {
        super(Admin.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void remove(Admin entity) {
        super.remove(entity);
    }
}
