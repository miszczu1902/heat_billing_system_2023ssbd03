package pl.lodz.p.it.ssbd2023.ssbd03.exceptions.mappers;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ErrorResponseDTO;

@Provider
public class ExceptionClientErrorJsonMapper implements ExceptionMapper<ClientErrorException> {
    @Override
    public Response toResponse(ClientErrorException exception) {
        int statusCode = exception.getResponse().getStatus();
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                statusCode,
                exception.getMessage());

        return Response.status(statusCode)
                .entity(errorResponseDTO)
                .type(MediaType.APPLICATION_JSON).build();
    }
}
