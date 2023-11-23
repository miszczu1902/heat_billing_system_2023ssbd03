package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.mappers;

import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ErrorResponseDTO;

@Provider
public class ExceptionValidationJsonMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException exception) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                Response.Status.BAD_REQUEST.getStatusCode(),
                exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponseDTO)
                .type(MediaType.APPLICATION_JSON).build();
    }
}