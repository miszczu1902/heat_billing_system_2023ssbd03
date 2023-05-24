package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AnnualBalance;

@Stateless
public class BalanceFacade extends AbstractFacade<AnnualBalance> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public BalanceFacade() {
        super(AnnualBalance.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void edit(AnnualBalance entity) {
        super.edit(entity);
    }

    @Override
    public void create(AnnualBalance entity) {
        super.create(entity);
    }

    @Override
    public void remove(AnnualBalance entity) {
        super.remove(entity);
    }
}
