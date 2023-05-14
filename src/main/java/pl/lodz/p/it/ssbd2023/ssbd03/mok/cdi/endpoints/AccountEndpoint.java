package pl.lodz.p.it.ssbd2023.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.*;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Admin;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Owner;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangeSelfPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ChangePhoneNumberDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.CreateOwnerDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.PersonalData;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services.AccountService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.EtagValidator;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.AccountMapper;

import java.util.List;

@Path("/accounts")
@RequestScoped
public class AccountEndpoint {
    @Inject
    private AccountService accountService;

    @Inject
    MessageSigner messageSigner;

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
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/phone-number")
    @RolesAllowed(Roles.OWNER)
    public Response changePhoneNumber(@Valid ChangePhoneNumberDTO changePhoneNumberDTO, @Context HttpServletRequest request) {
        String etag = request.getHeader("If-Match");
        accountService.changePhoneNumber(changePhoneNumberDTO.getPhoneNumber(), etag);
        return Response.noContent().build();
    }

    @PATCH
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/password")
    @RolesAllowed({Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public Response changeSelfPassword(@NotNull @Valid ChangeSelfPasswordDTO changeSelfPasswordDTO,
                                       @Context HttpServletRequest request) {
        String etag = request.getHeader("If-Match");
        accountService.changeSelfPassword(changeSelfPasswordDTO.getOldPassword(), changeSelfPasswordDTO.getNewPassword(),
                changeSelfPasswordDTO.getRepeatedNewPassword(), etag);
        return Response.noContent().build();
    }

    @PATCH
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/password")
    @RolesAllowed({Roles.ADMIN})
    public Response changeUserPassword(@NotNull @Valid ChangeUserPasswordDTO changeUserPasswordDTO,
                                       @NotBlank @PathParam("username") String username,
                                       @Context HttpServletRequest request) {
        String etag = request.getHeader("If-Match");
        accountService.changeUserPassword(username, changeUserPasswordDTO.getNewPassword(),
                changeUserPasswordDTO.getRepeatedNewPassword(), etag);
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
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public Response editPersonalData(@NotNull @Valid EditPersonalDataDTO editPersonalDataDTO,
                                     @Context HttpServletRequest request) {
        String etag = request.getHeader("If-Match");
        accountService.editSelfPersonalData(editPersonalDataDTO.getFirstName(), editPersonalDataDTO.getSurname(), etag);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{username}/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public Response getPersonalData(@PathParam("username") String username) {
        PersonalData personalData = accountService.getPersonalData(username);
        PersonalDataDTO personalDataDTO = new PersonalDataDTO(personalData.getId().getId(), personalData.getVersion(),
                personalData.getFirstName(), personalData.getSurname());
        return Response.status(Response.Status.OK)
                .entity(personalDataDTO)
                .header("ETag", messageSigner.sign(personalDataDTO))
                .build();
    }

    @PATCH
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response editUserPersonalData(@NotNull @Valid EditPersonalDataDTO editPersonalDataDTO,
                                         @PathParam("username") String username,
                                         @Context HttpServletRequest request) {
        String etag = request.getHeader("If-Match");
        accountService.editUserPersonalData(username,
                editPersonalDataDTO.getFirstName(),
                editPersonalDataDTO.getSurname(),
                etag);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @EtagValidator
    @Path("/{username}/disable")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response disableUserAccount(@PathParam("username") String username, @Context HttpServletRequest request) {
        String etag = request.getHeader("If-Match");
        accountService.disableUserAccount(username, etag);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @EtagValidator
    @Path("/{username}/enable")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response enableUserAccount(@PathParam("username") String username, @Context HttpServletRequest request) {
        String etag = request.getHeader("If-Match");
        accountService.enableUserAccount(username, etag);
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
        return Response.ok()
                .entity(accountInfoDTO)
                .header("ETag", messageSigner.sign(accountInfoDTO))
                .build();
    }

    @PATCH
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/email")
    @RolesAllowed({Roles.MANAGER, Roles.ADMIN})
    public Response changeUserEmail(@NotNull @Valid ChangeEmailDTO changeEmailDTO,
                                    @PathParam("username") String username,
                                    @Context HttpServletRequest request) {
        String etag = request.getHeader("If-Match");
        accountService.changeUserEmail(changeEmailDTO.getNewEmail(), username, etag);
        return Response.noContent().build();
    }

    @GET
    @Path("/self/owner")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.OWNER)
    public Response getMyOwnerAccount() {
        final Owner owner = accountService.getOwner();
        final OwnerDTO ownerDTO = AccountMapper.createOwnerDTOEntity(owner,
                accountService.getPersonalData(owner.getAccount().getUsername()));
        return Response.ok()
                .entity(ownerDTO)
                .header("ETag", messageSigner.sign(ownerDTO))
                .build();
    }

    @GET
    @Path("/self/manager")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.MANAGER)
    public Response getMyManagerAccount() {
        final Manager manager = accountService.getManager();
        final ManagerDTO managerDTO = AccountMapper.createManagerDTOEntity(manager,
                accountService.getPersonalData(manager.getAccount().getUsername()));
        return Response.ok()
                .entity(managerDTO)
                .header("ETag", messageSigner.sign(managerDTO))
                .build();
    }

    @GET
    @Path("/self/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Response getMyAdminAccount() {
        final Admin admin = accountService.getAdmin();
        final AdminDTO adminDTO = AccountMapper.createAdminDTOEntity(admin,
                accountService.getPersonalData(admin.getAccount().getUsername()));
        return Response.ok()
                .entity(adminDTO)
                .header("ETag", messageSigner.sign(adminDTO))
                .build();
    }

    @PATCH
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/email")
    @RolesAllowed({Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public Response changeSelfEmail(@NotNull @Valid ChangeEmailDTO changeEmailDTO, @Context HttpServletRequest request) {
        String etag = request.getHeader("If-Match");
        accountService.changeSelfEmail(changeEmailDTO.getNewEmail(), etag);
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