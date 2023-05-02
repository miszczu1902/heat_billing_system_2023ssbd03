package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.mappers;

import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
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
public class ExceptionJsonMapper implements ExceptionMapper<Throwable> {
    Logger logger = Logger.getLogger(ExceptionJsonMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        AppException exceptionToJson = getAppExceptionToThrow(exception);

        int statusCode = exceptionToJson.getResponse().getStatus();
        String message = exceptionToJson.getMessage();
        Response.Status status = Response.Status.fromStatusCode(statusCode);
        String title = status.toString().toUpperCase().replace(" ", "_");

        return Response.status(status)
                .entity(new ErrorResponseDTO(title, statusCode, message))
                .type(MediaType.APPLICATION_JSON).build();
    }

    private AppException getAppExceptionToThrow(Throwable exception) {
        try {
            throw exception;
        } catch (AppException ae) {
            return ae;
        } catch (WebApplicationException wae) {
            return AppException.createAppException(wae.getMessage(), wae.getCause());
        } catch (EJBAccessException | AccessLocalException ae) {
            return AppException.createAccessDeniedException(ae.getCause());
        } catch (Throwable throwable) {
            logger.log(Level.SEVERE, "ERROR_UNKNOWN", throwable);
            return AppException.createAppException(throwable.getCause());
        }
    }
}
