package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccessLevelMapping;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccessLevelMappingFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.PersonalDataFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;

import java.time.LocalDateTime;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountServiceImpl implements AccountService {
    @Inject
    private AccountFacade accountFacade;

    @Inject
    private PersonalDataFacade personalDataFacade;

    @Inject
    private AccessLevelMappingFacade accessLevelMappingFacade;

    @Override
    public void createOwner(PersonalData personalData, String phoneNumber) {
        Account account = personalData.getId();

        account.setPassword(BcryptHashGenerator.generateHash(account.getPassword()));

        AccessLevelMapping owner = new Owner(phoneNumber);
        owner.setAccount(account);
        owner.setAccessLevel("owner");

        account.getAccessLevels().add(owner);
        account.setRegisterDate(LocalDateTime.now());

        accountFacade.create(account);
        accessLevelMappingFacade.create(owner);
        personalDataFacade.create(personalData);
    }

}
