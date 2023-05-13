package pl.lodz.p.it.ssbd2023.ssbd03.util.etag;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;

import static java.lang.Long.parseLong;

@ApplicationScoped
public class MessageSigner {

    private final String secretKey = String.valueOf(parseLong(LoadConfig.loadPropertyFromConfig("etag.secretKey")));
    private JWSSigner jwsSigner;

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
