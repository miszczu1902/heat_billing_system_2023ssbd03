package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.heatDistributionCentre;


import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;


public class ConsumptionAddException extends AppException{
    public ConsumptionAddException(String message, Response.Status status) {
        super(message, status);
    }
}