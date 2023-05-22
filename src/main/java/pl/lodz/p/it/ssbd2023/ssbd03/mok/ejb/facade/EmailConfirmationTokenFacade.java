package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.EmailConfirmationToken;

import java.time.LocalDateTime;
import java.util.List;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class EmailConfirmationTokenFacade extends AbstractFacade<EmailConfirmationToken> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    public EmailConfirmationTokenFacade() {
        super(EmailConfirmationToken.class);
    }

    @RolesAllowed({Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public EmailConfirmationToken getActivationTokenByTokenValue(String tokenValue) {
        TypedQuery<EmailConfirmationToken> query = em.createNamedQuery("EmailConfirmationToken.getActivationTokenByTokenValue", EmailConfirmationToken.class);
        query.setParameter("tokenValue", tokenValue);
        return query.getSingleResult();
    }

    public List<EmailConfirmationToken> getExpiredNewEmailTokensList() {
        TypedQuery<EmailConfirmationToken> query = em.createNamedQuery("EmailConfirmationToken.findAllUnconfirmedEmails", EmailConfirmationToken.class);
        query.setParameter("currentTime", LocalDateTime.now(TIME_ZONE));
        return query.getResultList();
    }
}
