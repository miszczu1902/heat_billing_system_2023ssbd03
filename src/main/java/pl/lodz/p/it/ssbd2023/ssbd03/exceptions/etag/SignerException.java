package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.etag;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

public class SignerException extends AppException {
    public SignerException(String message, Response.Status status) {
        super(message, status);
    }
}
