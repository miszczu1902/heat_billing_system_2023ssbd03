package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterEntry;

@Stateless
public class HotWaterEntryFacade extends AbstractFacade<HotWaterEntry> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public HotWaterEntryFacade() {
        super(HotWaterEntry.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void edit(HotWaterEntry entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER,Roles.OWNER})
    public void create(HotWaterEntry entity) {
        super.create(entity);
    }

    @Override
    public void remove(HotWaterEntry entity) {
        super.remove(entity);
    }
}