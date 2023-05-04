package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

@ApplicationException(rollback = true)
public class DatabaseException extends AppException {
   public DatabaseException() {
        super(Response.Status.INTERNAL_SERVER_ERROR, ERROR_UNKNOWN);
    }
}
