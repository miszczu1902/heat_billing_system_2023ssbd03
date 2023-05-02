package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@ApplicationException(rollback = true)
public class PasswordsNotSameException extends AppException {
    private static String MESSAGE = "Passwords are not the same"; //TODO tu trzeba zrobić resource bundle

    public PasswordsNotSameException(Response.Status status, Throwable cause) {
        super(MESSAGE, status, cause);
    }
}
