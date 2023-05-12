package pl.lodz.p.it.ssbd2023.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.*;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Admin;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;
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
    public Response activateAccountFromEmail(@NotNull @Valid TokenFromEmailDTO activationTokenDTO) {
        accountService.confirmAccountFromActivationLink(activationTokenDTO.getActivationToken());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    @RolesAllowed(Roles.GUEST)
    public Response authenticate(@Valid LoginDTO loginDTO) {
        try {
            final String token = accountService.authenticate(loginDTO.getUsername(), loginDTO.getPassword());
            accountService.updateLoginData(loginDTO.getUsername(), true);
            return Response.ok().header("Bearer", token).build();
        } catch (Exception ex) {
            accountService.updateLoginData(loginDTO.getUsername(), false);
            throw AppException.invalidCredentialsException();
        }
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
    public Response changeSelfPassword(@NotNull @Valid ChangeSelfPasswordDTO changeSelfPasswordDTO) {
        try {
            accountService.changeSelfPassword(changeSelfPasswordDTO.getOldPassword(), changeSelfPasswordDTO.getNewPassword(),
                    changeSelfPasswordDTO.getRepeatedNewPassword());
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
    @Path("/{username}/password")
    @RolesAllowed({Roles.ADMIN})
    public Response changeUserPassword(@NotNull @Valid ChangeUserPasswordDTO changeUserPasswordDTO,
                                       @NotBlank @PathParam("username") String username) {
        accountService.changeUserPassword(username, changeUserPasswordDTO.getNewPassword(),
                changeUserPasswordDTO.getRepeatedNewPassword());
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reset-password")
    @RolesAllowed({Roles.GUEST})
    public Response resetPassword(@NotNull @Valid ResetPasswordDTO resetPasswordDTO) {
        accountService.resetPassword(resetPasswordDTO.getUsername());
        return Response.noContent().build();
    }

    @PATCH
    @Path("/reset-password-from-email")
    @RolesAllowed(Roles.GUEST)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ResetPasswordFromEmail(@NotNull @Valid ResetPasswordFromEmailDTO resetPasswordTokenDTO) {
        accountService.changePasswordFromResetPasswordLink(resetPasswordTokenDTO.getResetPasswordToken(),
                resetPasswordTokenDTO.getNewPassword(), resetPasswordTokenDTO.getRepeatedNewPassword());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public Response editPersonalData(@NotNull @Valid PersonalDataDTO personalDataDTO) {
        accountService.editSelfPersonalData(personalDataDTO.getFirstName(), personalDataDTO.getSurname());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{username}/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public Response getUserPersonalData(@PathParam("username") String username) {
        PersonalData personalData = accountService.getUserPersonalData(username);
        return Response.ok().entity(new PersonalDataDTO(personalData.getFirstName(), personalData.getSurname())).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response editUserPersonalData(@NotNull @Valid PersonalDataDTO personalDataDTO, @PathParam("username") String username) {
        accountService.editUserPersonalData(username, personalDataDTO.getFirstName(), personalDataDTO.getSurname());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/disable")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response disableUserAccount(@PathParam("username") String username) {
        accountService.disableUserAccount(username);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/enable")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response enableUserAccount(@PathParam("username") String username) {
        accountService.enableUserAccount(username);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @Path("/add-access-level-manager")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Response addAccessLevelManager(@NotNull @Valid AddAccessLevelManagerDTO addAccessLevelManagerDTO) {
        accountService.addAccessLevelManager(addAccessLevelManagerDTO.getUsername(), addAccessLevelManagerDTO.getLicense());
        return Response.noContent().build();
    }

    @PATCH
    @Path("/add-access-level-owner")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Response addAccessLevelOwner(@NotNull @Valid AddAccessLevelOwnerDTO addAccessLevelOwnerDTO) {
        accountService.addAccessLevelOwner(addAccessLevelOwnerDTO.getUsername(), addAccessLevelOwnerDTO.getPhoneNumber());
        return Response.noContent().build();
    }

    @PATCH
    @Path("/add-access-level-admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Response addAccessLevelAdmin(@NotNull @Valid AddAccessLevelAdminDTO addAccessLevelAdminDTO) {
        accountService.addAccessLevelAdmin(addAccessLevelAdminDTO.getUsername());
        return Response.noContent().build();
    }

    @PATCH
    @Path("/revoke-access-level")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Response revokeAccessLevel(@NotNull @Valid RevokeAccessLevelDTO revokeAccessLevelDTO) {
        accountService.revokeAccessLevel(revokeAccessLevelDTO.getUsername(), revokeAccessLevelDTO.getAccessLevel());
        return Response.noContent().build();
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

    @GET
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response getAccount(@PathParam("username") String username) {
        final Account account = accountService.getAccount(username);
        final AccountInfoDTO accountInfoDTO = AccountMapper.createAccountInfoDTOEntity(account);
        return Response.ok().entity(accountInfoDTO).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/email")
    @RolesAllowed({Roles.MANAGER, Roles.ADMIN})
    public Response changeUserEmail(@NotNull @Valid ChangeEmailDTO changeEmailDTO, @PathParam("username") String username) {
        accountService.changeUserEmail(changeEmailDTO.getNewEmail(), username);
        return Response.noContent().build();
    }

    @GET
    @Path("/self/owner")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.OWNER)
    public Response getMyOwnerAccount() {
        final Owner owner = accountService.getOwner();
        final OwnerDTO ownerDTO = AccountMapper.createOwnerDTOEntity(owner, accountService.getPersonalData());
        return Response.ok().entity(ownerDTO).build();
    }

    @GET
    @Path("/self/manager")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.MANAGER)
    public Response getMyManagerAccount() {
        final Manager manager = accountService.getManager();
        final ManagerDTO managerDTO = AccountMapper.createManagerDTOEntity(manager, accountService.getPersonalData());
        return Response.ok().entity(managerDTO).build();
    }

    @GET
    @Path("/self/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Response getMyAdminAccount() {
        final Admin admin = accountService.getAdmin();
        final AdminDTO adminDTO = AccountMapper.createAdminDTOEntity(admin, accountService.getPersonalData());
        return Response.ok().entity(adminDTO).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/email")
    @RolesAllowed({Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public Response changeSelfEmail(@NotNull @Valid ChangeEmailDTO changeEmailDTO) {
        accountService.changeSelfEmail(changeEmailDTO.getNewEmail());
        return Response.noContent().build();
    }

    @PATCH
    @Path("/self/confirm-new-email")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public Response confirmNewEmail(@NotNull @Valid TokenFromEmailDTO activationTokenDTO) {
        accountService.confirmNewEmailAccountFromActivationLink(activationTokenDTO.getActivationToken());
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}