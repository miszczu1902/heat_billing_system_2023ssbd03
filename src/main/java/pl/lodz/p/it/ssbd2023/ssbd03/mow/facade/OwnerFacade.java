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
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class OwnerFacade extends AbstractFacade<Owner> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public OwnerFacade() {
        super(Owner.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @RolesAllowed({Roles.MANAGER})
    public Owner findById(Long id) {
        TypedQuery<Owner> tq = em.createNamedQuery("Owner.findById", Owner.class);
        tq.setParameter("id", id);
        return tq.getSingleResult();
    }
}