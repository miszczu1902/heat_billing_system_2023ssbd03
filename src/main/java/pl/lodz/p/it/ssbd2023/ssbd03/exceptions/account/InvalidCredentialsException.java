package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class InvalidCredentialsException extends AppException {
    public InvalidCredentialsException(String message, Response.Status status) {
        super(message, status);
    }
}