package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.LoginData;

@Boundary
@Transactional(value = Transactional.TxType.MANDATORY, rollbackOn = AppException.class)
public class LoginDataFacade extends AbstractFacade<LoginData> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    EntityManager em;

    public LoginDataFacade() {
        super(LoginData.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @RolesAllowed(Roles.GUEST)
    public LoginData findById(Account account) {
        TypedQuery<LoginData> tq = em.createNamedQuery("LoginData.findById", LoginData.class);
        tq.setParameter("id", account);
        return tq.getSingleResult();
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public void edit(LoginData entity) {
        super.edit(entity);
    }
}
