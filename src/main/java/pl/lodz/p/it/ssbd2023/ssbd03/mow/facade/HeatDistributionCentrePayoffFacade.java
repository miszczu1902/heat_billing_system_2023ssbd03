package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.config.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.HeatDistributionCentrePayoff;

public class HeatDistributionCentrePayoffFacade extends AbstractFacade<HeatDistributionCentrePayoff> {
    @PersistenceContext(unitName = "ssbd03mow")
    private EntityManager em;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public HeatDistributionCentrePayoffFacade() {
        super(HeatDistributionCentrePayoff.class);
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
