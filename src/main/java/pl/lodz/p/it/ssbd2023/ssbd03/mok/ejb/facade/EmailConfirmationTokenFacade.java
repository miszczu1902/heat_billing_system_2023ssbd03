package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.EmailConfirmationToken;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;

import java.time.LocalDateTime;
import java.util.List;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
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
        try {
            return query.getSingleResult();
        } catch (PersistenceException pe) {
            if (pe instanceof NoResultException) {
                throw AppException.createNoResultException(pe.getCause());
            }
            throw AppException.createDatabaseException();
        }
    }

    public List<EmailConfirmationToken> getExpiredNewEmailTokensList() {
        TypedQuery<EmailConfirmationToken> query = em.createNamedQuery("EmailConfirmationToken.findAllUnconfirmedEmails", EmailConfirmationToken.class);
        query.setParameter("currentTime", LocalDateTime.now(TIME_ZONE));
        return query.getResultList();
    }
}
