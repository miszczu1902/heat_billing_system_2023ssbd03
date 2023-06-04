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
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AnnualBalance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.MonthPayoff;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

import java.util.ArrayList;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class BalanceFacade extends AbstractFacade<AnnualBalance> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public BalanceFacade() {
        super(AnnualBalance.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public void edit(AnnualBalance entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void create(AnnualBalance entity) {
        super.create(entity);
    }

    @Override
    public void remove(AnnualBalance entity) {
        super.remove(entity);
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public MonthPayoff findByOwner() {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public AnnualBalance findReportByOwner() {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public List<AnnualBalance> findReports() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public AnnualBalance find(Object id) {
        return super.find(id);
    }

    @RolesAllowed({Roles.MANAGER})
    public List<AnnualBalance> getListOfAnnualBalances(int pageNumber, int pageSize, List<Place> placeList) {
        List<Long> idList = new ArrayList<>();
        for (Place place : placeList) {
            Long id = place.getId();
            idList.add(id);
        }
        TypedQuery<AnnualBalance> tq = em.createNamedQuery("AnnualBalance.findAllByPlace", AnnualBalance.class);
        tq.setParameter("ids", idList);
        if (pageNumber != 0) {
            tq.setFirstResult((pageNumber - 1) * pageSize);
            tq.setMaxResults(pageSize);
        }

        return tq.getResultList();
    }
}
