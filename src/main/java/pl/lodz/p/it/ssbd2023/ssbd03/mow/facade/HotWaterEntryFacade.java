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

import java.time.LocalDate;

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
    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public void edit(HotWaterEntry entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
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

    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public HotWaterEntry getEntryWithCheckingIfHotWaterEntryCouldBeInsertedOrOverwritten(Long placeId, boolean checkIfCouldBeOverwritten) {
        final LocalDate now = LocalDate.now();
        TypedQuery<HotWaterEntry> tq;

        if (checkIfCouldBeOverwritten) {
            tq = em.createNamedQuery("HotWaterEntry.checkIfHotWaterEntryCouldBeOverwritten", HotWaterEntry.class);
        } else {
            tq = em.createNamedQuery("HotWaterEntry.checkIfHotWaterEntryCouldBeInserted", HotWaterEntry.class);
        }
        tq.setParameter("year", now.getYear());
        tq.setParameter("month", now.getMonthValue());
        tq.setParameter("placeId", placeId);

        final List<HotWaterEntry> resultList = tq.getResultList();
        return resultList.isEmpty() ? null :resultList.get(0);
    }

    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public List<HotWaterEntry> getHotWaterEntriesByPlaceId(Long placeId){
        TypedQuery<HotWaterEntry> tq = em.createNamedQuery("HotWaterEntry.getListOfHotWaterEntriesForPlace", HotWaterEntry.class);
        tq.setParameter("id", placeId);
        return tq.getResultList();
    }
}