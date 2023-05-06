package pl.lodz.p.it.ssbd2023.ssbd03.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.IdentityStore;
import pl.lodz.p.it.ssbd2023.ssbd03.util.BcryptHashGenerator;

@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:app/jdbc/ssbd03auth",
        callerQuery = "SELECT password from glassfish_auth_view WHERE username = ?",
        groupsQuery = "SELECT access_level from glassfish_auth_view WHERE username = ?",
        hashAlgorithm = BcryptHashGenerator.class
)
@ApplicationScoped
public class AuthIdentityStore implements IdentityStore {
}
