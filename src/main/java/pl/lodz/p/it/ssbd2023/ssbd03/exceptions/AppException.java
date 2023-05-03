package pl.lodz.p.it.ssbd2023.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account.AccountExistsException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account.PasswordsNotSameException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database.OptimisticLockAppException;

@ApplicationException(rollback = true)
public class AppException extends WebApplicationException {
    protected final static String ERROR_UNKNOWN = "ERROR_UNKNOWN";
    protected final static String ERROR_GENERAL_PERSISTENCE = "ERROR_GENERAL_PERSISTENCE";
    protected final static String ERROR_OPTIMISTIC_LOCK = "ERROR_OPTIMISTIC_LOCK";
    protected final static String ERROR_ACCESS_DENIED = "ERROR_ACCESS_DENIED";
    protected final static String ERROR_TRANSACTION_ROLLEDBACK = "ERROR_TRANSACTION_ROLLEDBACK";
    protected final static String ERROR_ACCOUNT_NOT_REGISTERED = "ERROR_ACCOUNT_NOT_REGISTERED";
    protected final static String ERROR_VALIDATION_FAILED = "ERROR_VALIDATION_FAILED";

    protected final static String ERROR_PASSWORDS_NOT_SAME_MESSAGE = "Passwords are not the same"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_EMAIL_NOT_UNIQUE_MESSAGE = "Email not unique"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_USERNAME_NOT_UNIQUE_MESSAGE = "Username not unique"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_PHONE_NUMBER_NOT_UNIQUE_MESSAGE = "Phone number not unique"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ACCOUNT_EXISTS_MESSAGE = "Account already exists"; //TODO - tu trzeba zrobić resource bundle

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

    public static AppException createValidationException(Throwable cause) {
        return new AppException(Response.Status.BAD_REQUEST, ERROR_VALIDATION_FAILED, cause);
    }

    public static DatabaseException createDatabaseException() {
        return new DatabaseException();
    }

    public static PasswordsNotSameException createPasswordsNotSameException() {
        return new PasswordsNotSameException(ERROR_PASSWORDS_NOT_SAME_MESSAGE, Response.Status.BAD_REQUEST, null);
    }

    public static MailNotSentException createMailNotSentException() {
        return new MailNotSentException();
    }

    public static AccountExistsException createAccountExistsException(Throwable cause) {
        if (cause instanceof ConstraintViolationException) {
            if (((ConstraintViolationException) cause).getConstraintName().contains("unique_email")) {
                return new AccountExistsException(AppException.ERROR_EMAIL_NOT_UNIQUE_MESSAGE, Response.Status.CONFLICT, cause);
            } else if (((ConstraintViolationException) cause).getConstraintName().contains("unique_username")) {
                return new AccountExistsException(AppException.ERROR_USERNAME_NOT_UNIQUE_MESSAGE, Response.Status.CONFLICT, cause);
            } else {
                return new AccountExistsException(AppException.ERROR_PHONE_NUMBER_NOT_UNIQUE_MESSAGE, Response.Status.CONFLICT, cause);
            }
        } else {
            return new AccountExistsException(ERROR_ACCOUNT_EXISTS_MESSAGE, Response.Status.CONFLICT, cause);
        }
    }

    public static OptimisticLockAppException createOptimisticLockAppException() {
        return new OptimisticLockAppException();
    }

}
