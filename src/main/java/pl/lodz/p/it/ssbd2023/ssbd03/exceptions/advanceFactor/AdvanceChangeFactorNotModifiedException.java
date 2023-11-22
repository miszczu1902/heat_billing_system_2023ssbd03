package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.advanceFactor;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class AdvanceChangeFactorNotModifiedException extends AppException {
    public AdvanceChangeFactorNotModifiedException(String message, Response.Status status) {
        super(message, status);
    }
}
