package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.mappers;

import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ErrorResponseDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class AppExceptionJsonMapper implements ExceptionMapper<Throwable> {
    Logger logger = Logger.getLogger(AppExceptionJsonMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        try {
            try {
                throw exception;
            } catch (WebApplicationException wae) {
                throw AppException.createAppException(wae.getMessage(), wae.getCause());
            } catch (EJBAccessException | AccessLocalException ae) {
                throw AppException.createAccessDeniedException(ae.getCause());
            } catch (Throwable throwable) {
                logger.log(Level.SEVERE, "ERROR_UNKNOWN", throwable);
                throw AppException.createAppException(throwable.getCause());
            }
        } catch (AppException | ValidationException exceptionToJson) {
            int statusCode = 400;

            if (exceptionToJson instanceof AppException) {
                statusCode  = ((AppException) exceptionToJson).getResponse().getStatus();
            }

            String message = exceptionToJson.getCause() == null ? exceptionToJson.getMessage() : exceptionToJson.getCause().toString();
            Response.Status status = Response.Status.fromStatusCode(statusCode);
            String title = status.toString().toUpperCase().replace(" ", "_");

            return Response.status(status)
                    .entity(new ErrorResponseDTO(title, statusCode, message))
                    .type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }
}
