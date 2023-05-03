package pl.lodz.p.it.ssbd2023.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangePhoneNumberDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.PersonalDataDTO;
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

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/phone-number")
    @RolesAllowed(Roles.OWNER)
    public Response changePhoneNumber(@Valid ChangePhoneNumberDTO changePhoneNumberDTO) {
        try {
            accountService.changePhoneNumber(changePhoneNumberDTO);
            return Response.ok("Phone number changed").build();
        } catch (IllegalStateException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex).build();
        }
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("self/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public Response editPersonalData(@Valid PersonalDataDTO personalDataDTO){
        accountService.editPersonalData(personalDataDTO);
        return Response.status(Response.Status.OK).build();
    }
}
