package pl.lodz.p.it.ssbd2023.ssbd03.exceptions;

import jakarta.ws.rs.core.Response;

public class DatabaseException extends AppException {
   public DatabaseException() {
        super(Response.Status.INTERNAL_SERVER_ERROR, ERROR_UNKNOWN);
    }
}
