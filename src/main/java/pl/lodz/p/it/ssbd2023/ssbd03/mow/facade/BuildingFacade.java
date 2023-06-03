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
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class BuildingFacade extends AbstractFacade<Building> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

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


    @RolesAllowed({Roles.MANAGER})
    public Building findById(){throw new UnsupportedOperationException();}

    @RolesAllowed({Roles.MANAGER})
    public List<Building> getListOfBuildingsWithPaging(int pageNumber, int pageSize) {
        TypedQuery<Building> tq = em.createNamedQuery("Building.findAll", Building.class);

        if (pageNumber != 0) {
            tq.setFirstResult((pageNumber - 1) * pageSize);
            tq.setMaxResults(pageSize);
        }

        return tq.getResultList();
    }

}
