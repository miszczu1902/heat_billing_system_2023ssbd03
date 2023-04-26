package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;

import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void create(Account entity) {
        super.create(entity);
    }

    @Override
    public void edit(Account entity) {
        super.edit(entity);
    }

    @Override
    public void remove(Account entity) {
        super.remove(entity);
    }

    @Override
    public Account find(Object id) {
        return super.find(id);
    }

    public List<Account> findByLoginOrEmailOrPesel(String username, String email) {
        TypedQuery<Account> findQuery = em.createNamedQuery("Account.findByLoginOrEmailOrPesel", Account.class);
        findQuery.setParameter("username", username);
        findQuery.setParameter("email", email);

        return findQuery.getResultList();
    }
}
