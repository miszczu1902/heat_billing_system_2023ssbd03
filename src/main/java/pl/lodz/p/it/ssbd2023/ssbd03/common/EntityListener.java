package pl.lodz.p.it.ssbd2023.ssbd03.common;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.security.enterprise.SecurityContext;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccessLevelMappingFacade;

import java.security.Principal;
import java.time.LocalDateTime;

import static pl.lodz.p.it.ssbd2023.ssbd03.config.ApplicationConfig.TIME_ZONE;

public class EntityListener {
    @PrePersist
    public void initCreatedBy(AbstractEntity entity) {
        final Principal principal = CDI.current().select(SecurityContext.class).get().getCallerPrincipal();
        final AccessLevelMappingFacade accessLevelMappingFacade = CDI.current().select(AccessLevelMappingFacade.class).get();
        final String username = principal.getName();
        final Account account = accessLevelMappingFacade.findByUsernameForEntityListener(username);
        entity.setCreatedBy(account);
        entity.setCreationDateTime(LocalDateTime.now(TIME_ZONE));
    }

    @PreUpdate
    public void initLastModifiedBy(AbstractEntity entity) {
        final Principal principal = CDI.current().select(SecurityContext.class).get().getCallerPrincipal();
        final AccessLevelMappingFacade accessLevelMappingFacade = CDI.current().select(AccessLevelMappingFacade.class).get();
        final String username = principal.getName();
        final Account account = accessLevelMappingFacade.findByUsernameForEntityListener(username);
        entity.setLastModifiedBy(account);
        entity.setLastModificationDateTime(LocalDateTime.now(TIME_ZONE));
    }
}
