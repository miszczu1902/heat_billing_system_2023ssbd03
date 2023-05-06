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
import pl.lodz.p.it.ssbd2023.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account.AccountPasswordException;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.*;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private ManagerFacade managerFacade;

    @Inject
    private AdminFacade adminFacade;

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
                .orElseThrow(() -> new IllegalArgumentException("Personal data not found"));
    }

    @Override
    public PersonalData getPersonalData(Manager manager) {
        return personalDataFacade.find(manager.getId())
                .orElseThrow(() -> new IllegalArgumentException("Personal data not found"));
    }

    @Override
    public PersonalData getPersonalData(Admin admin) {
        return personalDataFacade.find(admin.getId())
                .orElseThrow(() -> new IllegalArgumentException("Personal data not found"));
    }

    @Override
    public Owner getOwner(Long id) {
        return ownerFacade.find(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner data not found."));
    }

    @Override
    public Manager getManager(Long id) {
        return managerFacade.find(id)
                .orElseThrow(() -> new IllegalArgumentException("Manager data not found."));
    }

    @Override
    public Admin getAdmin(Long id) {
        return adminFacade.find(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin data not found."));
    }

    @Override
    public Owner getOwner() {
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByLogin(username);
        final Owner owner = (Owner) account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Owner)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Account is not an Owner."));
        return owner;
    }

    @Override
    public Manager getManager() {
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByLogin(username);
        final Manager manager = (Manager) account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Manager)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Account is not an Owner."));
        return manager;
    }

    @Override
    public Admin getAdmin() {
        final String username = securityContext.getCallerPrincipal().getName();
        final Account account = accountFacade.findByLogin(username);
        final Admin admin = (Admin) account.getAccessLevels().stream()
                .filter(accessLevel -> accessLevel instanceof Admin)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Account is not an Owner."));
        return admin;
    }

    @Override
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
