package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@ApplicationException(rollback = true)
public class OptimisticLockAppException extends AppException {
    public OptimisticLockAppException() {
        super(Response.Status.CONFLICT, ERROR_OPTIMISTIC_LOCK);
    }
}
