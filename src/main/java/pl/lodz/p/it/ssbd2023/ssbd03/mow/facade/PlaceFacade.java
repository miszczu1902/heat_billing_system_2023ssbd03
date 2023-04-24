package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Place;

@Stateless
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
    public void edit(Place entity) {
        super.edit(entity);
    }

    @Override
    public void create(Place entity) {
        super.create(entity);
    }

    @Override
    public void remove(Place entity) {
        super.remove(entity);
    }
}
