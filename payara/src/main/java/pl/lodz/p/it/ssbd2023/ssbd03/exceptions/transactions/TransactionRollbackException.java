package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.transactions;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

public class TransactionRollbackException extends AppException {
    public TransactionRollbackException() {
        super(ERROR_TRANSACTION_ROLLEDBACK, Response.Status.INTERNAL_SERVER_ERROR );
    }
}
