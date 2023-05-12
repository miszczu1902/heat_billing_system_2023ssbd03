package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.mappers;

import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ErrorResponseDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ExceptionJsonMapper implements ExceptionMapper<Throwable> {
    Logger logger = Logger.getLogger(ExceptionJsonMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        try {
            try {
                throw exception;
            } catch (EJBAccessException | AccessLocalException fe) {
                throw AppException.createNotAllowedActionException();
            } catch (AppException ae) {
                throw ae;
            } catch (Throwable throwable) {
                logger.log(Level.SEVERE, "ERROR_UNKNOWN", throwable);
                throw AppException.createAppException(throwable.getCause());
            }
        } catch (AppException exceptionToJson) {
            int statusCode = exceptionToJson.getResponse().getStatus();
            Response.Status status = Response.Status.fromStatusCode(statusCode);

            return Response.status(status)
                    .entity(new ErrorResponseDTO(statusCode, exceptionToJson.getMessage()))
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }
}
