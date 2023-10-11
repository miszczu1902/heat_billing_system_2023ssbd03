package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.place;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

//@ApplicationException(rollback = true)
public class NoPlaceFoundException extends AppException {
    public NoPlaceFoundException(String message, Response.Status status) {
        super(message, status);
    }
}