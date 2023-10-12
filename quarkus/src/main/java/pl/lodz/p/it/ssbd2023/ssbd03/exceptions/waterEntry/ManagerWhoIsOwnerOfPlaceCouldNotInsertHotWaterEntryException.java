package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.waterEntry;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class ManagerWhoIsOwnerOfPlaceCouldNotInsertHotWaterEntryException extends AppException {
    public ManagerWhoIsOwnerOfPlaceCouldNotInsertHotWaterEntryException(String message, Response.Status status) {
        super(message, status);
    }
}
