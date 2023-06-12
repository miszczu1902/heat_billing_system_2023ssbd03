package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ManagerFacade extends AbstractFacade<Manager> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public ManagerFacade() {
        super(Manager.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @RolesAllowed(Roles.MANAGER)
    public Manager findManagerByUsername(String username) {
        {
            TypedQuery<Manager> tq = em.createNamedQuery("Manager.findByUsername", Manager.class);
            tq.setParameter("username", username);
            if (tq.getResultList().isEmpty()) {
                throw AppException.createAccountIsNotManagerException();
            }
            return tq.getSingleResult();
        }
    }
}