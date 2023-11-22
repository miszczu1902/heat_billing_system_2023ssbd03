package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.place;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class ManagerCouldNotEditOwnedPlaceException extends AppException {
    public ManagerCouldNotEditOwnedPlaceException(String message, Response.Status status) {
        super(message, status);
    }
}
