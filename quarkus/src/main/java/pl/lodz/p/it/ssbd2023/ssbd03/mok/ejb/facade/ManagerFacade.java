package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Manager;

@Boundary
@Transactional(value = Transactional.TxType.MANDATORY, rollbackOn = AppException.class)
public class ManagerFacade extends AbstractFacade<Manager> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    EntityManager em;

    public ManagerFacade() {
        super(Manager.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @RolesAllowed(Roles.ADMIN)
    public boolean findByLicense(String license, String username) {
        TypedQuery<Manager> tq = em.createNamedQuery("Manager.findByLicenseAndWithoutUsername", Manager.class);
        tq.setParameter("license", license);
        tq.setParameter("username", username);
        try {
            tq.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void remove(Manager entity) {
        super.remove(entity);
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void create(Manager entity) {
        super.create(entity);
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void edit(Manager entity) {
        super.edit(entity);
    }
}