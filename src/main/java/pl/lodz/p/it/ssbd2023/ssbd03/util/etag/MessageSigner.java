package pl.lodz.p.it.ssbd2023.ssbd03.util.etag;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@ApplicationScoped
public class MessageSigner {
    private JWSSigner jwsSigner;

    @Inject
    @ConfigProperty(name = "ETag.secretKey")
    String secretKey;

    @PostConstruct
    public void init() {
        try {
            jwsSigner = new MACSigner(secretKey);
        } catch (KeyLengthException e) {
            throw AppException.createSignerException();
        }
    }

    public String sign(Signable signable) {
        try {
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(signable.messageToSign()));
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw AppException.createSignerException();
        }
    }
}
