package pl.lodz.p.it.ssbd2023.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account.*;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.building.BuildingCommunalAreaBiggerOrEqualTotalAreaException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database.OptimisticLockAppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.etag.SignerException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.etag.VerifierException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.personalData.PersonalDataConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.query.NoQueryResultException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.role.NotAllowedActionException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.transactions.TransactionRollbackException;

@ApplicationException(rollback = true)
public class AppException extends WebApplicationException {
    protected final static String ERROR_UNKNOWN = "exception.error.unknown";
    protected final static String ERROR_GENERAL_PERSISTENCE = "exception.error.general_persistence";
    protected final static String ERROR_OPTIMISTIC_LOCK = "exception.error.optimistic_lock";
    protected final static String ERROR_ACCESS_DENIED = "exception.error.access_denied";
    protected final static String ERROR_TRANSACTION_ROLLEDBACK = "exception.error.transaction_rolledback";
    protected final static String ERROR_ACCOUNT_NOT_REGISTERED = "exception.error.account_not_registered";

    protected final static String ERROR_PASSWORDS_NOT_SAME_MESSAGE = "exception.password.not_same_message";
    protected final static String ERROR_PASSWORDS_OLD_AND_NEW_SAME_EXCEPTION = "exception.password.old_and_new_same";
    protected final static String ERROR_PASSWORD_OLD_INCORRECT_EXCEPTION = "exception.password.old_incorrect";

    protected final static String ERROR_EMAIL_NOT_UNIQUE_MESSAGE = "exception.account.email_not_unique";
    protected final static String ERROR_USERNAME_NOT_UNIQUE_MESSAGE = "exception.account.username_not_unique";
    protected final static String ERROR_PHONE_NUMBER_NOT_UNIQUE_MESSAGE = "exception.account.phone_number_not_unique";
    protected final static String ERROR_CURRENT_PHONE_NUMBER = "exception.account.current_phone_number";
    protected final static String ERROR_CURRENT_EMAIL = "exception.account.current_email";
    protected final static String ERROR_ACCOUNT_IS_NOT_OWNER = "exception.account.account_is_not_owner";
    protected final static String ERROR_ACCOUNT_EXISTS_MESSAGE = "exception.account.account_exists";
    protected final static String ERROR_ACCOUNT_NOT_EXISTS_MESSAGE = "exception.account.account_not_exists";
    protected final static String ERROR_RESULT_NOT_FOUND = "exception.account.result_not_found";
    protected final static String ERROR_TOKEN_NOT_FOUND = "exception.token.result_not_found";
    protected final static String ERROR_ACTION_NOT_ALLOWED = "exception.account.action_not_allowed";
    protected final static String ERROR_PERSONAL_DATA_VALIDATION = "exception.account.personal_data_validation";
    protected final static String ERROR_INVALID_CREDENTIALS = "exception.account.invalid_credentials";
    protected final static String ERROR_ADDING_AN_ACCESS_LEVEL_TO_THE_SAME_ADMIN_ACCOUNT = "exception.account.adding_an_access_level_to_the_same_admin_account";
    protected final static String ERROR_REVOKE_THE_ONLY_LEVEL_OF_ACCESS = "exception.account.revoke_the_only_level_of_access";
    protected final static String ERROR_ACCOUNT_IS_NOT_ADMIN = "exception.account.account_is_not_admin";
    protected final static String ERROR_MANAGER_CAN_NOT_CHANGE_ADMIN = "exception.account.manager_con_not_change_admin";
    protected final static String ERROR_ACCOUNT_IS_NOT_MANAGER = "exception.account.account_is_not_manager";
    protected final static String ERROR_ACCESS_LEVEL_IS_ALREADY_GRANTED = "exception.account.access_level_is_already_granted";
    protected final static String ERROR_LICENSE_NOT_UNIQUE_MESSAGE = "exception.account.license_not_unique";
    protected final static String ERROR_ACCOUNT_IS_NOT_ACTIVATED = "exception.account.account_is_not_activated";
    protected final static String ERROR_ACCOUNT_IS_BLOCKED = "exception.account.account_is_blocked";
    protected final static String ERROR_REVOKE_ACCESS_LEVEL_TO_THE_SAME_ADMIN_ACCOUNT = "exception.account.revoke_access_level_to_the_same_admin_account";
    protected final static String ERROR_ETAG_VERIFIER = "exception.account.etag_verifier";
    protected final static String ERROR_ETAG_SIGNER = "exception.account.etag_signer";
    protected final static String TOKEN_IS_NOT_VALID = "exception.account.not_valid_token";
    protected final static String COMMUNAL_AREA_EQUAL_OR_BIGGER_THAT_TOTAL_AREA = "exception.building.communal_area_equal_or_bigger_that_total_area";

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

    public static AppException createPersistenceException(Throwable cause) {
        return new AppException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_GENERAL_PERSISTENCE, cause);
    }

    public static AppException createLastTransactionRolledBackException() {
        return new AppException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_TRANSACTION_ROLLEDBACK);
    }

    public static NoQueryResultException createNoResultException(Throwable cause) {
        return new NoQueryResultException(ERROR_RESULT_NOT_FOUND, Response.Status.NOT_FOUND, cause);
    }
    public static AppException tokenNoResultException () {
        return new AppException(ERROR_TOKEN_NOT_FOUND, Response.Status.NOT_FOUND);
    }

    public static NotAllowedActionException createNotAllowedActionException() {
        return new NotAllowedActionException(Response.Status.METHOD_NOT_ALLOWED, ERROR_ACTION_NOT_ALLOWED);
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

    public static CurrentEmailException createCurrentEmailException() {
        return new CurrentEmailException(AppException.ERROR_CURRENT_EMAIL, Response.Status.CONFLICT);
    }

    public static AccountWithEmailExistsException createAccountWithEmailExistsException() {
        return new AccountWithEmailExistsException(AppException.ERROR_EMAIL_NOT_UNIQUE_MESSAGE, Response.Status.CONFLICT);
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

    public static BuildingCommunalAreaBiggerOrEqualTotalAreaException createCommunalAreaBiggerOrEqualTotalAreaException(){
        return new BuildingCommunalAreaBiggerOrEqualTotalAreaException(COMMUNAL_AREA_EQUAL_OR_BIGGER_THAT_TOTAL_AREA, Response.Status.FORBIDDEN);
    }
}
