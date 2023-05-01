package pl.lodz.p.it.ssbd2023.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class EntityNotExistException extends AppException {
    public EntityNotExistException() {
        super(Response.Status.CONFLICT, ERROR_ENTITY_NOT_FOUND);
    }
}
