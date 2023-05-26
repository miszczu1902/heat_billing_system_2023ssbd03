package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AnnualBalance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.MonthPayoff;

import java.time.Month;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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
    @RolesAllowed(Roles.MANAGER)
    public void edit(AnnualBalance entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void create(AnnualBalance entity) {
        super.create(entity);
    }

    @Override
    public void remove(AnnualBalance entity) {
        super.remove(entity);
    }

    @RolesAllowed({Roles.MANAGER,Roles.OWNER})
    public MonthPayoff findByOwner(){throw new UnsupportedOperationException();}

    @RolesAllowed({Roles.MANAGER,Roles.OWNER})
    public AnnualBalance findReportByOwner(){throw new UnsupportedOperationException();}

    @RolesAllowed({Roles.MANAGER,Roles.OWNER})
    public List<AnnualBalance> findReports(){throw new UnsupportedOperationException();}

    @Override
    @RolesAllowed({Roles.MANAGER})
    public AnnualBalance find(Object id) {
        return super.find(id);
    }
}
