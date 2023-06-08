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

import java.time.LocalDate;
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
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void create(HotWaterEntry entity) {
        super.create(entity);
    }

    @Override
    public void remove(HotWaterEntry entity) {
        super.remove(entity);
    }

    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public boolean checkIfHotWaterEntryWasInsertedOrCouldBeOverwritten(Long placeId) {
        return findNewestHotWaterEntryForPlace(placeId) != null;
    }

    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public HotWaterEntry findNewestHotWaterEntryForPlace(Long placeId) {
        TypedQuery<HotWaterEntry> tq = em.createNamedQuery("HotWaterEntry.checkIfHotWaterEntryCouldBeInserted", HotWaterEntry.class);
        tq.setParameter("date", LocalDate.now());
        tq.setParameter("placeId", placeId);

        List<HotWaterEntry> resultList = tq.getResultList();
        return !resultList.isEmpty() ? resultList.get(0) : null;
    }
}