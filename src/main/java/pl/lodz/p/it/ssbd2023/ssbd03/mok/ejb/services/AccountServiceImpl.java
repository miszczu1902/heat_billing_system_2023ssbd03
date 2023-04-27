package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.JwtGenerator;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccessLevelMappingFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.PersonalDataFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;

import java.time.LocalDateTime;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountServiceImpl extends AbstractService implements AccountService {
    @Inject
    private PersonalDataFacade personalDataFacade;

    @Inject
    private AccessLevelMappingFacade accessLevelMappingFacade;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private AccountFacade accountFacade;
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
    private final JwtGenerator jwtGenerator = new JwtGenerator();

    @Override
    public String authenticate(String username, String password) {


        if (accountFacade.findByLoginAndPassword(username,password) == null) {
            throw new IndexOutOfBoundsException();
        }
        UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredential(username, new Password(password));
//        CredentialValidationResult credentialValidationResult = identityStoreHandler.validate(usernamePasswordCredential);
        String[] roles ={"ADMIN","OWNER"};
        return jwtGenerator.generateJWT(username,roles);
    }
}
