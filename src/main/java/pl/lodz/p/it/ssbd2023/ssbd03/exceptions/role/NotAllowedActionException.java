package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.role;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@ApplicationException(rollback = true)
public class NotAllowedActionException extends AppException {
    public NotAllowedActionException(Response.Status status, String message) {
        super(status, message);
    }
}
