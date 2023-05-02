package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@ApplicationException(rollback = true)
public class AccountExistsException extends AppException {
    private static String MESSAGE = "Account already exists"; //TODO tu trzeba zrobić resource bundle

    public AccountExistsException(Response.Status status, Throwable cause) {
        super(MESSAGE, status, cause);
    }
}
