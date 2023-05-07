package pl.lodz.p.it.ssbd2023.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.*;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AccountForListDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.ErrorResponseDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.account.AccountPasswordException;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services.AccountService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.AccountMapper;

import java.util.List;

@Path("/accounts")
@RequestScoped
public class AccountEndpoint {
    @Inject
    private AccountService accountService;

    @POST
    @Path("/register")
    @RolesAllowed(Roles.GUEST)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerOwner(@NotNull @Valid CreateOwnerDTO createOwnerDTO) {
        if (!createOwnerDTO.getPassword().equals(createOwnerDTO.getRepeatedPassword())) {
            throw AppException.createPasswordsNotSameException();
        }
        accountService.createOwner(AccountMapper.createOwnerDTOToAccount(createOwnerDTO));
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/activate-from-email")
    @RolesAllowed(Roles.GUEST)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response activateAccountFromEmail(@NotNull @Valid ActivateAccountFromEmailDTO activationTokenDTO) {
        accountService.confirmAccountFromActivationLink(activationTokenDTO.getActivationToken());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    @RolesAllowed(Roles.GUEST)
    public Response authenticate(@Valid LoginDTO loginDTO) {
        final String token = accountService.authenticate(loginDTO.getUsername(), loginDTO.getPassword());
        return Response.ok().header("Bearer", token).build();
    }

    @GET
    @Path("/test")
    @RolesAllowed(Roles.ADMIN)
    public Response getTest() {
        return Response.ok().entity("test").build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/phone-number")
    @RolesAllowed(Roles.OWNER)
    public Response changePhoneNumber(@Valid ChangePhoneNumberDTO changePhoneNumberDTO) {
        accountService.changePhoneNumber(changePhoneNumberDTO.getPhoneNumber());
        return Response.noContent().build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/password")
    @RolesAllowed({Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public Response changeSelfPassword(@NotNull @Valid ChangePasswordDTO changePasswordDTO) {
        try {
            accountService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword(),
                    changePasswordDTO.getRepeatedNewPassword());
            return Response.noContent().build();
        } catch (AccountPasswordException e) {
            final ErrorResponseDTO errorResponseDTO =
                    new ErrorResponseDTO(
                            Response.Status.BAD_REQUEST.getStatusCode(),
                            e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponseDTO).build();
        }
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public Response editPersonalData(@NotNull @Valid PersonalDataDTO personalDataDTO) {
        accountService.editSelfPersonalData(personalDataDTO.getFirstName(), personalDataDTO.getSurname());
        return Response.status(Response.Status.OK).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response editUserPersonalData(@NotNull @Valid PersonalDataDTO personalDataDTO, @PathParam("username") String username) {
        accountService.editUserPersonalData(username, personalDataDTO.getFirstName(), personalDataDTO.getSurname());
        return Response.status(Response.Status.OK).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/disable")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response disableUserAccount(@PathParam("username") String username) {
        accountService.disableUserAccount(username);
        return Response.status(Response.Status.OK).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/enable")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response enableUserAccount(@PathParam("username") String username) {
        accountService.enableUserAccount(username);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response getListOfAccounts(@DefaultValue("username") @QueryParam("sortBy") String sortBy,
                                      @DefaultValue("0") @QueryParam("pageNumber") int pageNumber) {
        final List<AccountForListDTO> listOfAccounts = accountService.getListOfAccounts(sortBy, pageNumber)
                .stream()
                .map(AccountMapper::accountToAccountForListDTO)
                .toList();
        return Response.ok().entity(listOfAccounts).build();
    }
}
