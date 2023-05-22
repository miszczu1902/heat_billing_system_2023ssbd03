package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;

@Stateless
public class OwnerFacade extends AbstractFacade<Owner> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    public OwnerFacade() {
        super(Owner.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @RolesAllowed(Roles.OWNER)
    public boolean checkIfAnOwnerExistsByPhoneNumber(String phoneNumber) {
        TypedQuery<Owner> tq = em.createNamedQuery("Owner.findByPhoneNumber", Owner.class);
        tq.setParameter("phoneNumber", phoneNumber);
        try {
            tq.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @RolesAllowed(Roles.ADMIN)
    public boolean checkIfAnOwnerExistsByPhoneNumberAndWithoutUsername(String phoneNumber, String username) {
        TypedQuery<Owner> tq = em.createNamedQuery("Owner.findByPhoneNumberAndWithoutUsername", Owner.class);
        tq.setParameter("phoneNumber", phoneNumber);
        tq.setParameter("username", username);
        try {
            tq.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    @RolesAllowed(Roles.OWNER)
    public void edit(Owner entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed(Roles.ADMIN)
    public void remove(Owner entity) {
        super.remove(entity);
    }
}