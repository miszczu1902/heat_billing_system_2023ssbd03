package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.common.AppBaseException;

public class AccountPasswordException extends AppBaseException {
    public AccountPasswordException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }
}
