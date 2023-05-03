package pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import pl.lodz.p.it.ssbd2023.ssbd03.auth.JwtGenerator;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AccessLevelMapping;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;
import pl.lodz.p.it.ssbd2023.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.PersonalDataFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mail.MailSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({TrackerInterceptor.class})
public class AccountServiceImpl extends AbstractService implements AccountService, SessionSynchronization {
    @Inject
    private PersonalDataFacade personalDataFacade;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private MailSender mailSender;

    @Inject
    private JwtGenerator jwtGenerator;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Override
    @RolesAllowed(Roles.GUEST)
    public void createOwner(PersonalData personalData) {
        Account account = personalData.getId();
        account.setPassword(BcryptHashGenerator.generateHash(account.getPassword()));
        account.setRegisterDate(LocalDateTime.now());

        accountFacade.create(account);
        personalDataFacade.create(personalData);

        mailSender.sendEmail(account.getEmail(), "Test", "Test");
    }

    @Override
    @RolesAllowed(Roles.GUEST)
    public String authenticate(LoginDTO loginDTO) {
        Account account = accountFacade.findByUsername(loginDTO.getUsername());

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

}
