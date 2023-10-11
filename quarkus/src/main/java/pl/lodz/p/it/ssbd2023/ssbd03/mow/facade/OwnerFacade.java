package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.transaction.Transactional;
import jakarta.ejb.TransactionAttribute;
import jakarta.transaction.Transactional;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Boundary@Transactional(Transactional.TxType.MANDATORY)
 //@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class OwnerFacade extends AbstractFacade<Owner> {
    
    @PersistenceContext(unitName = "ssbd03mowPU")
    EntityManager em;

    public OwnerFacade() {
        super(Owner.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @RolesAllowed(Roles.MANAGER)
    public Owner findOwnerByUsername(String username) {
        TypedQuery<Owner> tq = em.createNamedQuery("Owner.findByUsername", Owner.class);
        tq.setParameter("username", username);
        if (tq.getResultList().isEmpty()) {
            throw AppException.createAccountIsNotOwnerException();
        }
        return tq.getSingleResult();
    }

    @RolesAllowed({Roles.MANAGER})
    public Owner findById(Long id) {
        TypedQuery<Owner> tq = em.createNamedQuery("Owner.findById", Owner.class);
        tq.setParameter("id", id);
        return tq.getSingleResult();
    }

    @RolesAllowed({Roles.MANAGER})
    public List<Owner> getListOfOwners() {
        TypedQuery<Owner> query = em.createNamedQuery("Owner.findAllOwners", Owner.class);
        return Optional.of(query.getResultList()).orElse(Collections.emptyList());
    }

}