package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.place;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class PredictedHotWaterConsumptionValueAlreadySetException extends AppException {
    public PredictedHotWaterConsumptionValueAlreadySetException(String message, Response.Status status) {
        super(message, status);
    }
}
