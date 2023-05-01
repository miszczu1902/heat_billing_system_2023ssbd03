package pl.lodz.p.it.ssbd2023.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class OptimisticLockAppException extends AppException {
    public OptimisticLockAppException() {
        super(Response.Status.CONFLICT, ERROR_OPTIMISTIC_LOCK);
    }
}
