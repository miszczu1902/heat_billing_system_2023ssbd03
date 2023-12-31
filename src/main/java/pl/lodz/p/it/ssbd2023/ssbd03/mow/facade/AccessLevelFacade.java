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

import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessLevelFacade extends AbstractFacade<AccessLevelMapping> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public AccessLevelFacade() {
        super(AccessLevelMapping.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Optional<Manager> findManagerByUsername(String username) {
        if (username != null) {
            TypedQuery<Account> tq = em.createNamedQuery("AccessLevelMapping.findByUsername", Account.class);
            tq.setParameter("username", username);
            Account singleResult = tq.getSingleResult();
            if (!singleResult.getAccessLevels().isEmpty() && !singleResult.getAccessLevels().stream()
                    .filter(role -> role instanceof Manager).toList().isEmpty()) {
                return Optional.of((Manager) singleResult.getAccessLevels().stream()
                        .filter(accessLevel -> accessLevel instanceof Manager).toList().get(0));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
