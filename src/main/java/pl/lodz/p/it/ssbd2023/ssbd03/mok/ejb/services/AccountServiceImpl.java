package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccessLevelMappingFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.PersonalDataFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;

import java.time.LocalDateTime;
import java.util.Optional;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountServiceImpl extends AbstractService implements AccountService {

    @Inject
    private AccountFacade accountFacade;
    @Inject
    private PersonalDataFacade personalDataFacade;

    @Inject
    private AccessLevelMappingFacade accessLevelMappingFacade;

    @Override
    public void createOwner(PersonalData personalData) {
        Account account = personalData.getId();

//        if (!accountFacade.findByLoginOrEmailOrPesel(account.getUsername(), account.getEmail()).isEmpty()) {
//            throw new EntityExistsException();
//        }

        account.setPassword(BcryptHashGenerator.generateHash(account.getPassword()));
        account.setRegisterDate(LocalDateTime.now());

        accessLevelMappingFacade.create(account.getAccessLevels().get(0));
        personalDataFacade.create(personalData);
    }

    @Override
    public PersonalData getPersonalData(Owner owner) {
        Optional<PersonalData> optionalPersonalData = personalDataFacade.find(owner.getId());
        if (optionalPersonalData.isPresent()) return optionalPersonalData.get();
        else throw new IllegalArgumentException("Konto nie odnalezione");
    }

    @Override
    public Account getAccount(Long id) {
        Optional<Account> optAccount = accountFacade.find(id);
        if (optAccount.isPresent()) return optAccount.get();
        else throw new IllegalArgumentException("Konto nie odnalezione");
    }

}
