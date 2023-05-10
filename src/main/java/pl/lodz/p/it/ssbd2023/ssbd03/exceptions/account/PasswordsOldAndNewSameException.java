package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@ApplicationException(rollback = true)
public class PasswordsOldAndNewSameException extends AppException {
    public PasswordsOldAndNewSameException(String message, Response.Status status, Throwable cause) {
        super(message, status, cause);
    }
}
