package pl.lodz.p.it.ssbd2023.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@ApplicationException(rollback = true)
public class AppException extends WebApplicationException {
    protected final static String ERROR_UNKNOWN = "ERROR_UNKNOWN";
    protected final static String ERROR_GENERAL_PERSISTENCE = "ERROR_GENERAL_PERSISTENCE";
    protected final static String ERROR_ENTITY_NOT_FOUND = "ERROR_ENTITY_NOT_FOUND";
    protected final static String ERROR_OPTIMISTIC_LOCK = "ERROR_OPTIMISTIC_LOCK";
    protected final static String ERROR_ACCESS_DENIED = "ERROR_ACCESS_DENIED";
    protected final static String ERROR_TRANSACTION_ROLLEDBACK = "ERROR_TRANSACTION_ROLLEDBACK";

    @Getter
    private Throwable cause;

    protected AppException(Response.Status status, String key, Throwable cause) {
        super(Response.status(status).entity(key).build());
        this.cause = cause;
    }

    protected AppException(Response.Status status, String key) {
        super(Response.status(status).entity(key).build());
    }

    public static AppException createAppException(Throwable cause) {
        return new AppException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_UNKNOWN, cause);
    }

    public static AppException createAppException(String key, Throwable cause) {
        return new AppException(Response.Status.INTERNAL_SERVER_ERROR, key, cause);
    }

    public static AppException createGeneralException(String key, Throwable cause) {
        return new AppException(Response.Status.INTERNAL_SERVER_ERROR, key, cause);
    }

    public static AppException createAccessDeniedException(Throwable cause) {
        return new AppException(Response.Status.FORBIDDEN, ERROR_ACCESS_DENIED, cause);
    }

    public static AppException createPersistenceException(Throwable cause) {
        return new AppException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_GENERAL_PERSISTENCE, cause);
    }

    public static AppException createLastTransactionRolledBackException() {
        return new AppException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_TRANSACTION_ROLLEDBACK);
    }

    public static EntityNotExistException createEntityNotExistException() {
        return new EntityNotExistException();
    }

    public static OptimisticLockAppException createOptimisticLockAppException() {
        return new OptimisticLockAppException();
    }

}
