package pl.lodz.p.it.ssbd2023.ssbd03.common;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccessLevelMappingFacade;

import java.security.Principal;
import java.time.LocalDateTime;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

@ApplicationScoped
@Transactional(Transactional.TxType.NOT_SUPPORTED)
public class EntityListener {
    @Inject
    AccessLevelMappingFacade accessLevelMappingFacade;

    @Inject
    JsonWebToken jsonWebToken;

    @PrePersist
    public void initCreatedBy(AbstractEntity entity) {
        final String username = jsonWebToken.getSubject();
        final Account account = accessLevelMappingFacade.findByUsernameForEntityListener(username);
        entity.setCreatedBy(account);
        entity.setCreationDateTime(LocalDateTime.now(TIME_ZONE));
    }

    @PreUpdate
    public void initLastModifiedBy(AbstractEntity entity) {
        final String username = jsonWebToken.getSubject();
        final Account account = accessLevelMappingFacade.findByUsernameForEntityListener(username);
        entity.setLastModifiedBy(account);
        entity.setLastModificationDateTime(LocalDateTime.now(TIME_ZONE));
    }
}