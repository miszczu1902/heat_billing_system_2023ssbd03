package pl.lodz.p.it.ssbd2023.ssbd03.mow.facade;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatDistributionCentrePayoff;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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
    @RolesAllowed({Roles.MANAGER})
    public void edit(HeatDistributionCentrePayoff entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({Roles.MANAGER})
    public void create(HeatDistributionCentrePayoff entity) {
        super.create(entity);
    }

    @Override
    @RolesAllowed(Roles.MANAGER)
    public HeatDistributionCentrePayoff find(Object id) {
        return super.find(id);
    }

    @RolesAllowed({Roles.MANAGER})
    public boolean checkIfRecordForThisMonthNotExists() {
        TypedQuery<HeatDistributionCentrePayoff> tq = em.createNamedQuery("HeatDistributionCentrePayoff.getPayoffByDate", HeatDistributionCentrePayoff.class);
        tq.setParameter("year", LocalDate.now().getYear());
        tq.setParameter("month", LocalDate.now().getMonthValue());

        return tq.getResultList().isEmpty();
    }

    @PermitAll
    public HeatDistributionCentrePayoff findLatestHeatDistributionCentrePayoff() {
        TypedQuery<HeatDistributionCentrePayoff> query = getEntityManager()
                .createNamedQuery("HeatDistributionCentrePayoff.getLatestHeatDistributionCentrePayoff", HeatDistributionCentrePayoff.class);
        return query.getSingleResult();
    }

    @RolesAllowed({Roles.MANAGER})
    public List<HeatDistributionCentrePayoff> findAllHeatDistributionCentrePayoff() {
        TypedQuery<HeatDistributionCentrePayoff> tq = em.createNamedQuery("HeatDistributionCentrePayoff.findAllHeatDistributionCentrePayoff", HeatDistributionCentrePayoff.class);

        return Optional.of(tq.getResultList()).orElse(Collections.emptyList());

    }
}
