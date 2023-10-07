package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import io.quarkus.hibernate.orm.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.AccessLevelMapping;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Manager;

import java.util.Optional;

@Boundary
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessLevelFacade extends AbstractFacade<AccessLevelMapping> {
    @Inject
    @PersistenceUnit("ssbd03mowPU") EntityManager em;

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
