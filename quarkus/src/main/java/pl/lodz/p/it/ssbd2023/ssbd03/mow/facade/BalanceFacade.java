package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;

import java.util.ArrayList;
import java.util.List;

@Boundary
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class BalanceFacade extends AbstractFacade<AnnualBalance> {
    
    @PersistenceContext(unitName = "ssbd03mowPU")
    EntityManager em;

    public BalanceFacade() {
        super(AnnualBalance.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @PermitAll
    public void edit(AnnualBalance entity) {
        super.edit(entity);
    }

    @Override
    @PermitAll
    public void create(AnnualBalance entity) {
        super.create(entity);
    }

    @Override
    public void remove(AnnualBalance entity) {
        super.remove(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public AnnualBalance find(Object id) {
        return super.find(id);
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public AnnualBalance findBalanceById(Long reportId) {
        TypedQuery<AnnualBalance> tq = em.createNamedQuery("AnnualBalance.findBalanceById", AnnualBalance.class);
        tq.setParameter("id", reportId);
        return tq.getSingleResult();
    }

    @RolesAllowed({Roles.MANAGER})
    public List<AnnualBalance> getListOfAnnualBalancesFromBuilding(int pageNumber, int pageSize, List<Place> placeList) {
        final List<Long> idList = new ArrayList<>();
        for (Place place : placeList) {
            idList.add(place.getId());
        }
        TypedQuery<AnnualBalance> tq = em.createNamedQuery("AnnualBalance.findAllBalancesByPlacesId", AnnualBalance.class);
        tq.setParameter("ids", idList);
        if (pageNumber != 0) {
            tq.setFirstResult((pageNumber - 1) * pageSize);
            tq.setMaxResults(pageSize);
        }

        return tq.getResultList();
    }

    @RolesAllowed({Roles.OWNER})
    public List<AnnualBalance> getListOfAnnualBalancesForOwner(int pageNumber, int pageSize, String username) {
        TypedQuery<AnnualBalance> tq = em.createNamedQuery("AnnualBalance.findAllBalancesByOwnerUsername", AnnualBalance.class);
        tq.setParameter("username", username);
        if (pageNumber != 0) {
            tq.setFirstResult((pageNumber - 1) * pageSize);
            tq.setMaxResults(pageSize);
        }

        return tq.getResultList();
    }

    @PermitAll
    public List<AnnualBalance> getListOfAnnualBalancesForYear(Short year) {
        TypedQuery<AnnualBalance> tq = em.createNamedQuery("AnnualBalance.findAllBalancesByYear", AnnualBalance.class);
        tq.setParameter("year", year);
        return tq.getResultList();
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public List<HotWaterAdvance> findAllHotWaterAdvancesForPlace(Long placeId, Integer year) {
        TypedQuery<HotWaterAdvance> tq = em.createNamedQuery("Advance.findAllHotWaterAdvancesForPlace", HotWaterAdvance.class);
        tq.setParameter("placeId", placeId);
        tq.setParameter("year", year);
        return tq.getResultList();
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public List<HeatingPlaceAndCommunalAreaAdvance> findAllHeatingPlaceAndCommunalAreaAdvancesForPlace(Long placeId, Integer year) {
        TypedQuery<HeatingPlaceAndCommunalAreaAdvance> tq = em.createNamedQuery("Advance.findAllHeatingPlaceAndCommunalAreaAdvancesForPlace", HeatingPlaceAndCommunalAreaAdvance.class);
        tq.setParameter("placeId", placeId);
        tq.setParameter("year", year);
        return tq.getResultList();
    }
}
