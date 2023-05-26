package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

import java.util.List;

@Stateless
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
    public List<Building> getAllBuildings(){throw new UnsupportedOperationException();}
    

}
