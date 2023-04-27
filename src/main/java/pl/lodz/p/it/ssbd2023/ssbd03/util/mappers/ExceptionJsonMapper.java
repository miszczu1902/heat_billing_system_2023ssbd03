package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ErrorResponseDTO;

@Provider
public class ExceptionJsonMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        StringBuilder message = new StringBuilder()
                .append(exception.getClass())
                .append(exception.getCause()).append(":")
                .append(exception.getCause().getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                Response.Status.INTERNAL_SERVER_ERROR.toString(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                message.toString());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponseDTO)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
