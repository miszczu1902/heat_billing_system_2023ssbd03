package pl.lodz.p.it.ssbd2023.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account.*;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database.OptimisticLockAppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.etag.SignerException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.etag.VerifierException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.personalData.PersonalDataConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.query.NoQueryResultException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.role.NotAllowedActionException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.transactions.TransactionRollbackException;

@ApplicationException(rollback = true)
public class AppException extends WebApplicationException {
    protected final static String ERROR_UNKNOWN = "ERROR_UNKNOWN";
    protected final static String ERROR_GENERAL_PERSISTENCE = "ERROR_GENERAL_PERSISTENCE";
    protected final static String ERROR_OPTIMISTIC_LOCK = "ERROR_OPTIMISTIC_LOCK";
    protected final static String ERROR_TRANSACTION_ROLLEDBACK = "ERROR_TRANSACTION_ROLLEDBACK";
    protected final static String ERROR_ACCOUNT_NOT_REGISTERED = "ERROR_ACCOUNT_NOT_REGISTERED";

    protected final static String ERROR_PASSWORDS_NOT_SAME_MESSAGE = "Passwords are not the same"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_PASSWORDS_OLD_AND_NEW_SAME_EXCEPTION = "Old and new passwords are the same"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_PASSWORD_OLD_INCORRECT_EXCEPTION = "Old password is incorrect"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_EMAIL_NOT_UNIQUE_MESSAGE = "Email not unique"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_USERNAME_NOT_UNIQUE_MESSAGE = "Username not unique"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_PHONE_NUMBER_NOT_UNIQUE_MESSAGE = "Phone number not unique"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_CURRENT_PHONE_NUMBER = "This is your current phone number"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_CURRENT_EMAIL = "This is your current email"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ACCOUNT_IS_NOT_OWNER = "This account is not the owner"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ACCOUNT_EXISTS_MESSAGE = "Account already exists"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ACCOUNT_NOT_EXISTS_MESSAGE = "Account with provided data not exists"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_RESULT_NOT_FOUND = "Query result not found"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ACTION_NOT_ALLOWED = "Action is not allowed with this privileges"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_PERSONAL_DATA_VALIDATION = "Each element of personal data can have up to 32 characters"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_INVALID_CREDENTIALS = "Invalid username or password"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ADDING_AN_ACCESS_LEVEL_TO_THE_SAME_ADMIN_ACCOUNT = "You can't give yourself permissions"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_REVOKE_THE_ONLY_LEVEL_OF_ACCESS = "One level of access cannot be taken away"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ACCOUNT_IS_NOT_ADMIN = "This account is not the admin"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_MANAGER_CAN_NOT_CHANGE_ADMIN = "This account does not have permission to change Admin email"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ACCOUNT_IS_NOT_MANAGER = "This account is not the manager"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ACCESS_LEVEL_IS_ALREADY_GRANTED = "This account already has this level of access"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_LICENSE_NOT_UNIQUE_MESSAGE = "License not unique"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ACCOUNT_IS_NOT_ACTIVATED = "This account is not activated"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ACCOUNT_IS_BLOCKED = "This account is blocked"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_REVOKE_ACCESS_LEVEL_TO_THE_SAME_ADMIN_ACCOUNT = "You cannot take away your permissions"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ETAG_VERIFIER = "Cannot verify eTag"; //TODO - tu trzeba zrobić resource bundle
    protected final static String ERROR_ETAG_SIGNER = "Cannot sign with eTag"; //TODO - tu trzeba zrobić resource bundle
    protected final static String TOKEN_IS_NOT_VALID = "Cannot verify token"; //TODO - tu trzeba zrobić resource bundle

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

    public AppException(String message, Response.Status status) {
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

    public static AppException createPersistenceException(Throwable cause) {
        return new AppException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_GENERAL_PERSISTENCE, cause);
    }

    public static AppException createLastTransactionRolledBackException() {
        return new AppException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_TRANSACTION_ROLLEDBACK);
    }

    public static NoQueryResultException createNoResultException(Throwable cause) {
        return new NoQueryResultException(ERROR_RESULT_NOT_FOUND, Response.Status.NOT_FOUND, cause);
    }

    public static NotAllowedActionException createNotAllowedActionException() {
        return new NotAllowedActionException(Response.Status.METHOD_NOT_ALLOWED, ERROR_ACTION_NOT_ALLOWED);
    }

    public static DatabaseException createDatabaseException() {
        return new DatabaseException();
    }

    public static PasswordsNotSameException createPasswordsNotSameException() {
        return new PasswordsNotSameException(ERROR_PASSWORDS_NOT_SAME_MESSAGE, Response.Status.BAD_REQUEST, null);
    }

    public static PasswordsOldAndNewSameException createSameOldAndNewPasswordException() {
        return new PasswordsOldAndNewSameException(ERROR_PASSWORDS_OLD_AND_NEW_SAME_EXCEPTION, Response.Status.CONFLICT, null);
    }

    public static PasswordOldIncorrectException createPasswordOldIncorrectException() {
        return new PasswordOldIncorrectException(ERROR_PASSWORD_OLD_INCORRECT_EXCEPTION, Response.Status.BAD_REQUEST, null);
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

    public static PersonalDataConstraintViolationException createPersonalDataConstraintViolationException() {
        return new PersonalDataConstraintViolationException();
    }

    public static AccountNotExistsException createAccountNotExistsException(Throwable cause) {
        return new AccountNotExistsException(AppException.ERROR_ACCOUNT_NOT_EXISTS_MESSAGE, Response.Status.NOT_FOUND, cause);
    }

    public static InvalidCredentialsException invalidCredentialsException() {
        return new InvalidCredentialsException(AppException.ERROR_INVALID_CREDENTIALS, Response.Status.UNAUTHORIZED);
    }

    public static OptimisticLockAppException createOptimisticLockAppException() {
        return new OptimisticLockAppException();
    }

    public static AccountIsNotOwnerException createAccountIsNotOwnerException() {
        return new AccountIsNotOwnerException(AppException.ERROR_ACCOUNT_IS_NOT_OWNER, Response.Status.FORBIDDEN);
    }

    public static CurrentPhoneNumberException createCurrentPhoneNumberException() {
        return new CurrentPhoneNumberException(AppException.ERROR_CURRENT_PHONE_NUMBER, Response.Status.CONFLICT);
    }

    public static AccountWithNumberExistsException createAccountWithNumberExistsException() {
        return new AccountWithNumberExistsException(AppException.ERROR_PHONE_NUMBER_NOT_UNIQUE_MESSAGE, Response.Status.CONFLICT);
    }

    public static CurrentPhoneNumberException createCurrentEmailException() {
        return new CurrentPhoneNumberException(AppException.ERROR_CURRENT_EMAIL, Response.Status.CONFLICT);
    }

    public static AccountWithNumberExistsException createAccountWithEmailExistsException() {
        return new AccountWithNumberExistsException(AppException.ERROR_EMAIL_NOT_UNIQUE_MESSAGE, Response.Status.CONFLICT);
    }

    public static AccessLevelToTheSameAdminAccountException addingAnAccessLevelToTheSameAdminAccount() {
        return new AccessLevelToTheSameAdminAccountException(AppException.ERROR_ADDING_AN_ACCESS_LEVEL_TO_THE_SAME_ADMIN_ACCOUNT, Response.Status.FORBIDDEN);
    }

    public static TheOnlyLevelOfAccessException revokeTheOnlyLevelOfAccess() {
        return new TheOnlyLevelOfAccessException(AppException.ERROR_REVOKE_THE_ONLY_LEVEL_OF_ACCESS, Response.Status.FORBIDDEN);
    }

    public static ManagerCanNotChangeAdminException createManagerCanNotChangeAdminException() {
        return new ManagerCanNotChangeAdminException(AppException.ERROR_MANAGER_CAN_NOT_CHANGE_ADMIN, Response.Status.FORBIDDEN);
    }

    public static AccountIsNotAdminException createAccountIsNotAdminException() {
        return new AccountIsNotAdminException(AppException.ERROR_ACCOUNT_IS_NOT_ADMIN, Response.Status.FORBIDDEN);
    }

    public static AccountIsNotManagerException createAccountIsNotManagerException() {
        return new AccountIsNotManagerException(AppException.ERROR_ACCOUNT_IS_NOT_MANAGER, Response.Status.FORBIDDEN);
    }

    public static AccessLevelIsAlreadyGrantedException theAccessLevelisAlreadyGranted() {
        return new AccessLevelIsAlreadyGrantedException(AppException.ERROR_ACCESS_LEVEL_IS_ALREADY_GRANTED, Response.Status.FORBIDDEN);
    }

    public static AccountWithLicenseExistsException createAccountWithLicenseExistsException() {
        return new AccountWithLicenseExistsException(AppException.ERROR_LICENSE_NOT_UNIQUE_MESSAGE, Response.Status.CONFLICT);
    }

    public static AccountIsNotActivatedException createAccountIsNotActivatedException() {
        return new AccountIsNotActivatedException(AppException.ERROR_ACCOUNT_IS_NOT_ACTIVATED, Response.Status.CONFLICT);
    }

    public static AccountIsBlockedException createAccountIsBlockedException() {
        return new AccountIsBlockedException(AppException.ERROR_ACCOUNT_IS_BLOCKED, Response.Status.CONFLICT);
    }

    public static RevokeAccessLevelToTheSameAdminAccountException revokeAnAccessLevelToTheSameAdminAccount() {
        return new RevokeAccessLevelToTheSameAdminAccountException(AppException.ERROR_REVOKE_ACCESS_LEVEL_TO_THE_SAME_ADMIN_ACCOUNT, Response.Status.FORBIDDEN);
    }

    public static VerifierException createVerifierException() {
        return new VerifierException(ERROR_ETAG_VERIFIER, Response.Status.BAD_REQUEST);
    }

    public static SignerException createSignerException() {
        return new SignerException(ERROR_ETAG_SIGNER, Response.Status.BAD_REQUEST);
    }
    public static InvalidTokenException tokenIsNotValidException() {
        return new InvalidTokenException(TOKEN_IS_NOT_VALID, Response.Status.BAD_REQUEST);
    }

    public static TransactionRollbackException createTransactionRollbackException() {
        return new TransactionRollbackException();
    }
}
