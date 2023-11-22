package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.personalData;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class PersonalDataConstraintViolationException extends AppException {
    public PersonalDataConstraintViolationException() {
        super(Response.Status.BAD_REQUEST, ERROR_PERSONAL_DATA_VALIDATION);
    }
}
