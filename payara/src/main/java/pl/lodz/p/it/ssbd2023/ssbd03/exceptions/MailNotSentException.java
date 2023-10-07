package pl.lodz.p.it.ssbd2023.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class MailNotSentException extends AppException {
    public MailNotSentException() {
        super(Response.Status.INTERNAL_SERVER_ERROR, ERROR_ACCOUNT_NOT_REGISTERED);
    }
}
