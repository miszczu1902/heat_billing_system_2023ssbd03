package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import pl.lodz.p.it.ssbd2023.ssbd03.auth.JwtGenerator;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangePhoneNumberDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccessLevelMapping;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account.AccountPasswordException;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccessLevelMappingFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.OwnerFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.PersonalDataFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountServiceImpl extends AbstractService implements AccountService {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private PersonalDataFacade personalDataFacade;

    @Inject
    private OwnerFacade ownerFacade;

    @Inject
    private AccessLevelMappingFacade accessLevelMappingFacade;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private SecurityContext securityContext;

    @Override
    public void createOwner(PersonalData personalData) {
        Account account = personalData.getId();

        account.setPassword(BcryptHashGenerator.generateHash(account.getPassword()));
        account.setRegisterDate(LocalDateTime.now());

        accessLevelMappingFacade.create(account.getAccessLevels().get(0));
        personalDataFacade.create(personalData);
    }

    @Inject
    private JwtGenerator jwtGenerator;

    @Override
    public String authenticate(LoginDTO loginDTO) {
        Account account = accountFacade.findByLogin(loginDTO.getUsername());

        if (BcryptHashGenerator.generateHash(loginDTO.getPassword()).equals(account.getPassword())) {
//            UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredential(loginDTO.getUsername(), new Password(loginDTO.getPassword()));
//            CredentialValidationResult credentialValidationResult = identityStoreHandler.validate(usernamePasswordCredential);

            List<String> roles = account.getAccessLevels().stream()
                    .map(AccessLevelMapping::getAccessLevel)
                    .collect(Collectors.toList());

            return jwtGenerator.generateJWT(loginDTO.getUsername(), roles);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void changePhoneNumber(ChangePhoneNumberDTO changePhoneNumberDTO) {
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByLogin(username);
        Owner owner = (Owner) account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Owner)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Account is not an Owner."));
        final String newPhoneNumber = changePhoneNumberDTO.getPhoneNumber();
        if (!newPhoneNumber.equals(owner.getPhoneNumber())) {
            if (accountFacade.findByPhoneNumber(newPhoneNumber) == null) {
                owner.setPhoneNumber(newPhoneNumber);
                ownerFacade.edit(owner);
            } else {
                throw new IllegalArgumentException("Phone number is already taken.");
            }
        } else {
            throw new IllegalArgumentException("The given number is taken by your account.");
        }
    }

    @Override
    public PersonalData getPersonalData(Owner owner) {
        return personalDataFacade.find(owner.getId())
                .orElseThrow(() -> new IllegalArgumentException("Dane osobowe nie odnalezione"));
    }

    @Override
    public Owner getOwner(Long id) {
        return ownerFacade.find(id)
                .orElseThrow(() -> new IllegalArgumentException("Dane osobowe nie odnalezione"));
    }

    @Override
    public Owner getOwner() {
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByLogin(username);
        Owner owner = (Owner) account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Owner)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Account is not an Owner."));
        return owner;
    }

    public void changePassword(String oldPassword, String newPassword, String newRepeatedPassword) throws AccountPasswordException {
        if (oldPassword.equals(newPassword)) {
            throw new AccountPasswordException("Old password and new password are the same.");
        }
        if (!newPassword.equals(newRepeatedPassword)) {
            throw new AccountPasswordException("New password and new repeated password are not the same.");
        }
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByLogin(username);
        if (!BcryptHashGenerator.generateHash(oldPassword).equals(account.getPassword())) {
            throw new AccountPasswordException("Old password is incorrect");
        }
        final String newPasswordHash = BcryptHashGenerator.generateHash(newPassword);
        account.setPassword(newPasswordHash);
        accountFacade.edit(account);
    }
}
