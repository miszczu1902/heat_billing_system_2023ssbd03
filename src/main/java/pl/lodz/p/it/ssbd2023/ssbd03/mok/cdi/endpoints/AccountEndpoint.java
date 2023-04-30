package pl.lodz.p.it.ssbd2023.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ErrorResponseDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services.AccountService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.converters.AccountConverter;

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("login")
    @RolesAllowed(Roles.GUEST)
    public Response authenticate(@Valid LoginDTO loginDTO) {
        String token = accountService.authenticate(loginDTO);
        return Response.ok().header("Bearer", token).build();
    }

    @GET
    @Path("test")
    @RolesAllowed(Roles.ADMIN)
    public Response getTest() {
        return Response.ok().entity("test").build();
    }
}