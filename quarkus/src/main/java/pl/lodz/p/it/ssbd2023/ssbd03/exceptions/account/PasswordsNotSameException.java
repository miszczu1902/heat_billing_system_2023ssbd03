package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class PasswordsNotSameException extends AppException {
    public PasswordsNotSameException(String message, Response.Status status, Throwable cause) {
        super(message, status, cause);
    }
}
