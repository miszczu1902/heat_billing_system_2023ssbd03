package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;

import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PersonalDataFacade extends AbstractFacade<PersonalData> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    public PersonalDataFacade() {
        super(PersonalData.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    public Optional<PersonalData> find(Long id) {
        return super.find(id);
    }
}
