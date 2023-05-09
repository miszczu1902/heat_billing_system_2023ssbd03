package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({TrackerInterceptor.class})
public class ResetPasswordTokenFacade extends AbstractFacade<ResetPasswordToken>  {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    public ResetPasswordTokenFacade() {
        super(ResetPasswordToken.class);
    }

    @RolesAllowed({Roles.GUEST, Roles.ADMIN})
    public List<ResetPasswordToken> findAllUnconfirmedAccounts() {
        TypedQuery<ResetPasswordToken> query = em.createNamedQuery("ResetPasswordToken.findAllWithAvailableAccounts", ResetPasswordToken.class);
        query.setParameter("date", LocalDateTime.now().minusDays(1));
        return Optional.of(query.getResultList()).orElse(Collections.emptyList());
    }

    @RolesAllowed({Roles.GUEST, Roles.ADMIN})
    public ResetPasswordToken getResetPasswordByTokenValue(String tokenValue) {
        TypedQuery<ResetPasswordToken> query = em.createNamedQuery("ResetPasswordToken.getResetPasswordTokenByTokenValue", ResetPasswordToken.class);
        query.setParameter("tokenValue", tokenValue);
        return query.getSingleResult();
    }

    @RolesAllowed({Roles.GUEST, Roles.ADMIN})
    public boolean checkIfResetPasswordTokenExistsByTokenValue(String tokenValue) {
        TypedQuery<ResetPasswordToken> tq = em.createNamedQuery("ResetPasswordToken.getResetPasswordTokenByTokenValue", ResetPasswordToken.class);
        tq.setParameter("tokenValue", tokenValue);
        try {
            tq.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}
