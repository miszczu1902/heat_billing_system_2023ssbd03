package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

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
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Boundary
//@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class BuildingFacade extends AbstractFacade<Building> {
    
    @PersistenceContext(unitName = "ssbd03mowPU")
    EntityManager em;

    public BuildingFacade() {
        super(Building.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void edit(Building entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void create(Building entity) {
        super.create(entity);
    }

    @Override
    public void remove(Building entity) {
        super.remove(entity);
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER})
    public Building findById(Long id) {
        TypedQuery<Building> tq = em.createNamedQuery("Building.findById", Building.class);
        tq.setParameter("id", id);
        return tq.getSingleResult();
    }

    @RolesAllowed({Roles.MANAGER})
    public List<Building> getListOfBuildingsWithPaging(int pageNumber, int pageSize) {
        TypedQuery<Building> tq = em.createNamedQuery("Building.findAll", Building.class);

        if (pageNumber != 0) {
            tq.setFirstResult((pageNumber - 1) * pageSize);
            tq.setMaxResults(pageSize);
        }

        return tq.getResultList();
    }

    @RolesAllowed({Roles.MANAGER, Roles.OWNER, Roles.GUEST})
    public List<Building> findAllBuildings() {
        TypedQuery<Building> tq = em.createNamedQuery("Building.findAll", Building.class);

        return Optional.of(tq.getResultList()).orElse(Collections.emptyList());
    }

}
