package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Admin;

@Boundary@Transactional(value = Transactional.TxType.MANDATORY, rollbackOn = AppException.class)
public class AdminFacade extends AbstractFacade<Admin> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    EntityManager em;

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

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void create(Admin entity) {
        super.create(entity);
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void edit(Admin entity) {
        super.edit(entity);
    }
}
