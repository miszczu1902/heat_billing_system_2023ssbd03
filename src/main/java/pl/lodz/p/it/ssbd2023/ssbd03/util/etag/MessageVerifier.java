package pl.lodz.p.it.ssbd2023.ssbd03.util.etag;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;
import java.text.ParseException;

@ApplicationScoped
public class MessageVerifier {
    private final String secretKey = LoadConfig.loadPropertyFromConfig("ETag.secretKey");
    private JWSVerifier jwsVerifier;

    @PostConstruct
    public void init() {
        try {
            jwsVerifier = new MACVerifier(secretKey);
        } catch (JOSEException e) {
            throw AppException.createVerifierException();
        }
    }

    public boolean validateSignature(String message) {
        try {
            final JWSObject jwsObject = JWSObject.parse(message);
            return jwsObject.verify(jwsVerifier);
        } catch (ParseException | JOSEException e) {
            throw AppException.createVerifierException();
        }
    }
}
