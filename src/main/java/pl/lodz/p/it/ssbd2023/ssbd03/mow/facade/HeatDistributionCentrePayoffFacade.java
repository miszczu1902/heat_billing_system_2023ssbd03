package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatDistributionCentrePayoff;

public class HeatDistributionCentrePayoffFacade extends AbstractFacade<HeatDistributionCentrePayoff> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public HeatDistributionCentrePayoffFacade() {
        super(HeatDistributionCentrePayoff.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void edit(HeatDistributionCentrePayoff entity) {
        super.edit(entity);
    }

    @Override
    public void create(HeatDistributionCentrePayoff entity) {
        super.create(entity);
    }
}
