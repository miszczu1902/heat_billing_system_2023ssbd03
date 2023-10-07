package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import io.quarkus.hibernate.orm.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Boundary
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PlaceFacade extends AbstractFacade<Place> {
    @Inject
    @PersistenceUnit("ssbd03mowPU")
    EntityManager em;

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

    @PermitAll
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
    public List<Place> findByOwner(Long id, int pageNumber, int pageSize) {
        TypedQuery<Place> tq = em.createNamedQuery("Place.findPlacesByOwner", Place.class);
        tq.setParameter("id", id);
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

    @PermitAll
    public List<Place> findAllPlaces() {
        TypedQuery<Place> tq = em.createNamedQuery("Place.findAllPlaces", Place.class);

        return Optional.of(tq.getResultList()).orElse(Collections.emptyList());
    }

    @PermitAll
    public List<Place> findAllPlacesAddedBeforeDate(LocalDateTime date) {
        TypedQuery<Place> tq = em.createNamedQuery("Place.findAllPlacesAddedBeforeDate", Place.class);
        tq.setParameter("date", date);

        return Optional.of(tq.getResultList()).orElse(Collections.emptyList());
    }

    @PermitAll
    public List<Place> findAllPlacesByBuildingIdAndNewerThanDate(Long buildingId, LocalDateTime date) {
        TypedQuery<Place> tq = em.createNamedQuery("Place.findAllPlacesByBuildingIdAndNewerThanDate", Place.class);
        tq.setParameter("buildingId", buildingId);
        tq.setParameter("date", date);

        return Optional.of(tq.getResultList()).orElse(Collections.emptyList());
    }
}
