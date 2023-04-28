package pl.lodz.p.it.ssbd2023.ssbd03.mok.cdi.endpoints;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.AccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ErrorResponseDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.facade.PersonalDataFacade;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services.AccountService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.converters.AccountConverter;

import java.security.Principal;

@Path("accounts")
@RequestScoped
public class AccountEndpoint {
    @Inject
    private AccountService accountService;

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerOwner(@NotNull @Valid CreateOwnerDTO createOwnerDTO) {
        try {
            if (!createOwnerDTO.getPassword().equals(createOwnerDTO.getRepeatedPassword())) {
                ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                        "Password does not match",
                        Response.Status.BAD_REQUEST.getStatusCode(),
                        "Passwords not same");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponseDTO)
                        .build();
            }

            accountService.createOwner(AccountConverter.createOwnerDTOToPersonalData(createOwnerDTO));
            return Response.status(Response.Status.CREATED).build();
        } catch (EntityExistsException e) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                    "Owner already exist",
                    Response.Status.CONFLICT.getStatusCode(),
                    "Owner with same username/email exist");
            return Response.status(Response.Status.CONFLICT)
                    .entity(errorResponseDTO)
                    .build();
        }
    }

    //path do zmiany, na ścieżkę po zalogowaniu użytkownika
    @GET
    @Path("self")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AccountDTO getMyAccount(Principal principal) {
        return AccountConverter.createOwnerDTOEntity((Owner) principal, accountService.getPersonalData((Owner) principal));
    }
}
