package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Boundary
@Transactional(value = Transactional.TxType.MANDATORY, rollbackOn = AppException.class)
public class ManagerFacade extends AbstractFacade<Manager> {

    @PersistenceContext(unitName = "ssbd03mowPU")
    EntityManager em;

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

    @PermitAll
    public List<Manager> getListOfManagers() {
        TypedQuery<Manager> query = em.createNamedQuery("Manager.findAllManagers", Manager.class);
        return Optional.of(query.getResultList()).orElse(Collections.emptyList());
    }
}