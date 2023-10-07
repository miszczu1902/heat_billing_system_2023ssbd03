package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.PermitAll;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PastQuarterHotWaterPayoff;

@Boundary
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PastQuarterHotWaterPayoffFacade extends AbstractFacade<PastQuarterHotWaterPayoff> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public PastQuarterHotWaterPayoffFacade() {
        super(PastQuarterHotWaterPayoff.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @PermitAll
    public void edit(PastQuarterHotWaterPayoff entity) {
        super.edit(entity);
    }

    @Override
    @PermitAll
    public void create(PastQuarterHotWaterPayoff entity) {
        super.create(entity);
    }
}
