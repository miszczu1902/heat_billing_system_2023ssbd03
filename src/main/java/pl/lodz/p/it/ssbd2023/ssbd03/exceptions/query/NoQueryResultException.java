package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.query;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class NoQueryResultException extends AppException {
    public NoQueryResultException(String message, Response.Status status, Throwable cause) {
        super(message, status, cause);
    }
}