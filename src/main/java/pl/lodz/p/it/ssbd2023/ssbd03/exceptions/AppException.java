package pl.lodz.p.it.ssbd2023.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account.AccountExistsException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account.PasswordsNotSameException;

@ApplicationException(rollback = true)
public class AppException extends WebApplicationException {
    protected final static String ERROR_UNKNOWN = "ERROR_UNKNOWN";
    protected final static String ERROR_GENERAL_PERSISTENCE = "ERROR_GENERAL_PERSISTENCE";
    protected final static String ERROR_OPTIMISTIC_LOCK = "ERROR_OPTIMISTIC_LOCK";
    protected final static String ERROR_ACCESS_DENIED = "ERROR_ACCESS_DENIED";
    protected final static String ERROR_TRANSACTION_ROLLEDBACK = "ERROR_TRANSACTION_ROLLEDBACK";
    protected final static String ERROR_ACCOUNT_NOT_REGISTERED = "ERROR_ACCOUNT_NOT_REGISTERED";
    protected final static String ERROR_PASSWORDS_NOT_SAME = "ERROR_PASSWORDS_NOT_SAME";

    @Getter
    private Throwable cause;

    protected AppException(Response.Status status, String key, Throwable cause) {
        super(Response.status(status).entity(key).build());
        this.cause = cause;
    }

    public AppException(String message, Response.Status status, Throwable cause) {
        super(message, status);
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

    public static DatabaseException createDatabaseException() {
        return new DatabaseException();
    }

    public static PasswordsNotSameException createPasswordsNotSameException() {
        return new PasswordsNotSameException(Response.Status.CONFLICT, null);
    }

    public static MailNotSentException createMailNotSentException() {
        return new MailNotSentException();
    }

    public static AccountExistsException createAccountExistsException(Throwable cause) {
        return new AccountExistsException(Response.Status.CONFLICT, cause);
    }

    public static OptimisticLockAppException createOptimisticLockAppException() {
        return new OptimisticLockAppException();
    }

}
