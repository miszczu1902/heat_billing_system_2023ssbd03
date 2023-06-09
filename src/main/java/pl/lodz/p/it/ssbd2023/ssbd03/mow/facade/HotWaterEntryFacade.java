package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterEntry;

import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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

    @RolesAllowed({Roles.MANAGER})
    public List<HotWaterEntry> getListOfHotWaterEntriesForPlace(Long id) {
        TypedQuery<HotWaterEntry> tq = em.createNamedQuery("HotWaterEntry.getListOfHotWaterEntriesForPlace", HotWaterEntry.class);
        tq.setParameter("id", id);
        tq.setMaxResults(2);
        return tq.getResultList();
    }
}