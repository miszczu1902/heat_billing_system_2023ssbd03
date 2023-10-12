package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.building;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class NoBuildingFoundException extends AppException {
    public NoBuildingFoundException(String message, Response.Status status) {
        super(message, status);
    }
}