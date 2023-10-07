package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.building;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

public class AddingPlaceToTheSameAccountException extends AppException {
    public AddingPlaceToTheSameAccountException(String message, Response.Status status) {
        super(message, status);
    }
}