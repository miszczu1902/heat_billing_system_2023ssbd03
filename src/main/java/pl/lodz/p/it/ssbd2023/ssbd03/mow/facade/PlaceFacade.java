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
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PlaceFacade extends AbstractFacade<Place> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public PlaceFacade() {
        super(Place.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public void edit(Place entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void create(Place entity) {
        super.create(entity);
    }

    @Override
    public void remove(Place entity) {
        super.remove(entity);
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Building findByPlaceNumber() {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed({Roles.MANAGER})
    public List<Place> findPlacesByBuildingId(Long id) {
        TypedQuery<Place> tq = em.createNamedQuery("Place.findPlacesByBuildingId", Place.class);
        tq.setParameter("id", id);
        return tq.getResultList();
    }

    @RolesAllowed({Roles.MANAGER})
    public List<Place> findByBuildingId(Long id, int pageNumber, int pageSize) {
        TypedQuery<Place> tq = em.createNamedQuery("Place.findPlacesByBuildingId", Place.class);
        tq.setParameter("id", id);

        if (pageNumber != 0) {
            tq.setFirstResult((pageNumber - 1) * pageSize);
            tq.setMaxResults(pageSize);
        }
        return tq.getResultList();
    }

    @RolesAllowed(Roles.OWNER)
    public List<Place> findByOwner(Account account, int pageNumber, int pageSize) {
        TypedQuery<Place> tq = em.createNamedQuery("Place.findPlacesByOwner", Place.class);
        tq.setParameter("id", account.getId());
        if (pageNumber != 0) {
            tq.setFirstResult((pageNumber - 1) * pageSize);
            tq.setMaxResults(pageSize);
        }
        return tq.getResultList();
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Place findPlaceById(Long id) {
        TypedQuery<Place> tq = em.createNamedQuery("Place.findById", Place.class);
        tq.setParameter("id", id);
        return tq.getSingleResult();
    }

    @RolesAllowed({Roles.OWNER})
    public List<Place> findByOwner() {
        throw new UnsupportedOperationException();
    }
}
