package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatingPlaceAndCommunalAreaAdvance;

import java.time.LocalDate;
import java.util.List;

@Boundary
@Transactional(value = Transactional.TxType.MANDATORY, rollbackOn = AppException.class)
public class HeatingPlaceAndCommunalAreaAdvanceFacade extends AbstractFacade<HeatingPlaceAndCommunalAreaAdvance> {

    @PersistenceContext(unitName = "ssbd03mowPU")
    EntityManager em;

    public HeatingPlaceAndCommunalAreaAdvanceFacade() {
        super(HeatingPlaceAndCommunalAreaAdvance.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @PermitAll
    public void edit(HeatingPlaceAndCommunalAreaAdvance entity) {
        super.edit(entity);
    }

    @Override
    @PermitAll
    public void create(HeatingPlaceAndCommunalAreaAdvance entity) {
        super.create(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public HeatingPlaceAndCommunalAreaAdvance find(Object id) {
        return super.find(id);
    }

    @RolesAllowed({Roles.MANAGER})
    public boolean checkIfAdvanceChangeFactorNotModified(Long buildingId, LocalDate date) {
        TypedQuery<HeatingPlaceAndCommunalAreaAdvance> tq = em.createNamedQuery(
                "HeatingPlaceAndCommunalAreaAdvance.getAllHeatingPlaceAndCommunalAreaAdvances",
                HeatingPlaceAndCommunalAreaAdvance.class);
        tq.setParameter("buildingId", buildingId);
        tq.setParameter("date", date);
        return tq.getResultList().isEmpty();
    }


    @PermitAll
    public HeatingPlaceAndCommunalAreaAdvance findTheNewestAdvanceChangeFactor(Long buildingId) {
        TypedQuery<HeatingPlaceAndCommunalAreaAdvance> tq = em.createNamedQuery("HeatingPlaceAndCommunalAreaAdvance.findTheNewestAdvanceChangeFactor", HeatingPlaceAndCommunalAreaAdvance.class);
        tq.setParameter("buildingId", buildingId);
        List<HeatingPlaceAndCommunalAreaAdvance> resultList = tq.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    @PermitAll
    public List<HeatingPlaceAndCommunalAreaAdvance> findLastAdvances() {
        TypedQuery<HeatingPlaceAndCommunalAreaAdvance> tq = em.createNamedQuery("HeatingPlaceAndCommunalAreaAdvance.findLastAdvances", HeatingPlaceAndCommunalAreaAdvance.class);
        tq.setParameter("year", LocalDate.now().getYear());
        tq.setParameter("month", LocalDate.now().minusMonths(1).getMonthValue());
        return tq.getResultList();
    }

    @PermitAll
    public HeatingPlaceAndCommunalAreaAdvance findTheNewestAdvanceChangeFactorByPlaceId(Long placeId) {
        TypedQuery<HeatingPlaceAndCommunalAreaAdvance> tq = em.createNamedQuery("HeatingPlaceAndCommunalAreaAdvance.findTheNewestAdvanceChangeFactorByPlaceId", HeatingPlaceAndCommunalAreaAdvance.class);
        tq.setParameter("placeId", placeId);
        List<HeatingPlaceAndCommunalAreaAdvance> resultList = tq.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }
}
