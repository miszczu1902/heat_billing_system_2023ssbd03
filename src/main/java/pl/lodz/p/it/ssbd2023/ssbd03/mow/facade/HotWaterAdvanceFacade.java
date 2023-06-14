package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterAdvance;

import java.time.LocalDate;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class HotWaterAdvanceFacade extends AbstractFacade<HotWaterAdvance> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public HotWaterAdvanceFacade() {
        super(HotWaterAdvance.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void edit(HotWaterAdvance entity) {
        super.edit(entity);
    }

    @Override
    @PermitAll
    public void create(HotWaterAdvance entity) {
        super.create(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public HotWaterAdvance find(Object id) {
        return super.find(id);
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public HotWaterAdvance findByDate(LocalDate localDate, Long placeId) {
        TypedQuery<HotWaterAdvance> tq = em.createNamedQuery("HotWaterAdvance.findByDate", HotWaterAdvance.class);
        tq.setParameter("date_", localDate);
        tq.setParameter("placeId", placeId);
        return tq.getSingleResult();
    }
}
