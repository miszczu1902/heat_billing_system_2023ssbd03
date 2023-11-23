package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.building;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class LackOfSpaceInTheBuildingException extends AppException {
    public LackOfSpaceInTheBuildingException(String message, Response.Status status) {
        super(message, status);
    }
}