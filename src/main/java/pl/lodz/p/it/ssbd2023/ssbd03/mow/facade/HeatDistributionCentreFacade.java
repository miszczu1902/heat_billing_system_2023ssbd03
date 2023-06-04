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
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatDistributionCentre;

import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class HeatDistributionCentreFacade extends AbstractFacade<HeatDistributionCentre> {
    @PersistenceContext(unitName = "ssbd03mowPU")
    private EntityManager em;

    public HeatDistributionCentreFacade() {
        super(HeatDistributionCentre.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void edit(HeatDistributionCentre entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void create(HeatDistributionCentre entity) {
        super.create(entity);
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public HeatDistributionCentre find(Object id) {
        return super.find(id);
    }

    @RolesAllowed({Roles.MANAGER})
    public List<HeatDistributionCentre> getListOfHeatDistributionCentre() {
        TypedQuery<HeatDistributionCentre> tq = em.createNamedQuery("HeatDistributionCentre.getListOfHeatDistributionCentre", HeatDistributionCentre.class);
        return tq.getResultList();
    }
}
