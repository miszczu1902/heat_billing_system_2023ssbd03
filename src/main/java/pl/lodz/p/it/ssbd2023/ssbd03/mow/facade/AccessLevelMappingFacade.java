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
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccessLevelMapping;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Manager;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessLevelMappingFacade extends AbstractFacade<AccessLevelMapping> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public AccessLevelMappingFacade() {
        super(AccessLevelMapping.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @RolesAllowed(Roles.MANAGER)
    public Manager findManagerByUsername(String username) {
        TypedQuery<Account> tq = em.createNamedQuery("AccessLevelMapping.findByUsername", Account.class);
        tq.setParameter("username", username);
            return (Manager) tq.getSingleResult().getAccessLevels().stream()
                    .filter(accessLevel -> accessLevel instanceof Manager).toList().get(0);
    }
}
