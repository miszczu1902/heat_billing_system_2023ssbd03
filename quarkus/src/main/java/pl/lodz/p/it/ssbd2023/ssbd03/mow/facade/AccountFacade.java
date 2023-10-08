package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Account;

@Boundary
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {
    
    @PersistenceContext(unitName = "ssbd03mowPU")
    EntityManager em;

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public void create(Account entity) {
        super.create(entity);
    }

    @Override
    @PermitAll
    public void edit(Account entity) {
        super.edit(entity);
    }

    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public Account findByUsername(String username) {
        TypedQuery<Account> tq = em.createNamedQuery("Account.findByUsername", Account.class);
        tq.setParameter("username", username);
        return tq.getSingleResult();
    }

    @Override
    @PermitAll
    public void remove(Account entity) {
        super.remove(entity);
    }
}