package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.etag;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@ApplicationException(rollback = true)
public class VerifierException extends AppException {
    public VerifierException(String message, Response.Status status) {
        super(message, status);
    }
}
