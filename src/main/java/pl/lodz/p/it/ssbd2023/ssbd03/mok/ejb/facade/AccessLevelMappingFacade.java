package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccessLevelMapping;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AccessLevelMappingFacade extends AbstractFacade<AccessLevelMapping> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    public AccessLevelMappingFacade() {
        super(AccessLevelMapping.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @PermitAll
    public Account findByUsernameForEntityListener(String username) {
        TypedQuery<Account> tq = em.createNamedQuery("AccessLevelMapping.findByUsername", Account.class);
        tq.setParameter("username", username);
        if (!tq.getResultList().isEmpty()) return tq.getResultList().get(0);
        else return null;
    }
}
