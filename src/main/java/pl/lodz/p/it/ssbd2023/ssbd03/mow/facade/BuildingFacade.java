package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;

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
    public void edit(Building entity) {
        super.edit(entity);
    }

    @Override
    public void create(Building entity) {
        super.create(entity);
    }

    @Override
    public void remove(Building entity) {
        super.remove(entity);
    }
}
