package pl.lodz.p.it.ssbd2023.ssbd03.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.IdentityStore;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.AccessLevelMapping;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.AccessLevelMappingFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;

import java.util.Set;
import java.util.stream.Collectors;

//@DatabaseIdentityStoreDefinition(
//        dataSourceLookup = "java:app/jdbc/ssbd03auth",
//        callerQuery = "SELECT password from glassfish_auth_view WHERE username = ?",
//        groupsQuery = "SELECT access_level from glassfish_auth_view WHERE username = ?",
//        hashAlgorithm = BcryptHashGenerator.class
//)
@ApplicationScoped
public class AuthIdentityStore implements IdentityStore {

    @Inject
    AccessLevelMappingFacade accessLevelMappingFacade;

    @Inject
    BcryptHashGenerator bcryptHashGenerator;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential user) {
            final String username = user.getCaller();
            final Account account = accessLevelMappingFacade.findByUsernameForEntityListener(username);
            CredentialValidationResult credentialValidationResult = new CredentialValidationResult(
                    account.getUsername(),
                    account.getAccessLevels()
                            .stream()
                            .map(AccessLevelMapping::getAccessLevel).collect(Collectors.toSet()));
            return (account.getIsActive() && account.getIsEnable()) && bcryptHashGenerator.verify(user.getPassword().getValue(), account.getPassword())
                    ? credentialValidationResult
                    : CredentialValidationResult.INVALID_RESULT;
        }

        return CredentialValidationResult.INVALID_RESULT;
    }

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        final String username = validationResult.getCallerPrincipal().getName();
        return accessLevelMappingFacade.findByUsernameForEntityListener(username)
                .getAccessLevels()
                .stream()
                .map(AccessLevelMapping::getAccessLevel).collect(Collectors.toSet());
    }

    @Override
    public int priority() {
        return 70;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return IdentityStore.super.validationTypes();
    }
}
