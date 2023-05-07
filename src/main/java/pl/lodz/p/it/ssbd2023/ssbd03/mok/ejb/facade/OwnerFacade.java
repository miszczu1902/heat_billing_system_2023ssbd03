package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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

    public boolean checkIfAnOwnerExistsByPhoneNumber(String phoneNumber) {
        TypedQuery<Owner> tq = em.createNamedQuery("Owner.findByPhoneNumber", Owner.class);
        tq.setParameter("phoneNumber", phoneNumber);
        try {
            tq.getSingleResult();
            throw AppException.createAccountWithNumberExistsException();
        } catch (NoResultException e) {
            return false;
        }
    }
}