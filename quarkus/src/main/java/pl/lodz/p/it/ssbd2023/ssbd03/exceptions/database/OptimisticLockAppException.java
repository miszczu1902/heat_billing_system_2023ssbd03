package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class OptimisticLockAppException extends AppException {
    public OptimisticLockAppException() {
        super(Response.Status.CONFLICT, ERROR_OPTIMISTIC_LOCK);
    }
}
