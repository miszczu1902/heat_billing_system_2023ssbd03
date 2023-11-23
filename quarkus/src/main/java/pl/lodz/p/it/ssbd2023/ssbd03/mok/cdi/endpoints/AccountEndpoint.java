package pl.lodz.p.it.ssbd2023.ssbd03.mok.cdi.endpoints;

import io.quarkus.security.identity.SecurityIdentity;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.*;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.*;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database.OptimisticLockAppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.transactions.TransactionRollbackException;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.ejb.services.AccountService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.EtagValidator;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.AccountMapper;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/accounts")
@RequestScoped
public class AccountEndpoint {
    @Inject
    AccountService accountService;

    @Inject
    @ConfigProperty(name = "tx.retries", defaultValue = "3")
    int txRetries;

    protected static final Logger LOGGER = Logger.getGlobal();

    @Inject
    MessageSigner messageSigner;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    RoutingContext routingContext;


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
            final String language = accountService.updateLoginData(loginDTO.getUsername(), true, routingContext.request().remoteAddress().host());
            return Response.ok().header("Bearer", token).header("Language", language).build();
        } catch (Exception ex) {
            accountService.updateLoginData(loginDTO.getUsername(), false,  routingContext.request().remoteAddress().host());
            throw AppException.invalidCredentialsException();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/refresh-token")
    @RolesAllowed({Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public Response refreshToken(RefreshTokenDTO refreshTokenDTO) {
        final String token = accountService.refreshToken(refreshTokenDTO.getToken());
        return Response.ok().header("Bearer", token).build();
    }

    @PATCH
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/phone-number")
    @RolesAllowed(Roles.OWNER)
    public Response changePhoneNumber(@Valid ChangePhoneNumberDTO changePhoneNumberDTO) {
        final String etag = routingContext.request().getHeader("If-Match");

        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.changePhoneNumber(
                        securityIdentity.getPrincipal().getName(),
                        changePhoneNumberDTO.getPhoneNumber(),
                        etag,
                        changePhoneNumberDTO.getVersion()
                );
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        accountService.changePhoneNumber(
                securityIdentity.getPrincipal().getName(),
                changePhoneNumberDTO.getPhoneNumber(),
                etag,
                changePhoneNumberDTO.getVersion()
        );
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/password")
    @RolesAllowed({Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public Response changeSelfPassword(@NotNull @Valid ChangeSelfPasswordDTO changeSelfPasswordDTO) {
        final String etag = routingContext.request().getHeader("If-Match");

        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.changeSelfPassword(
                        securityIdentity.getPrincipal().getName(),
                        changeSelfPasswordDTO.getOldPassword(),
                        changeSelfPasswordDTO.getNewPassword(),
                        changeSelfPasswordDTO.getRepeatedNewPassword(),
                        etag,
                        changeSelfPasswordDTO.getVersion());
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        accountService.changeSelfPassword(
                securityIdentity.getPrincipal().getName(),
                changeSelfPasswordDTO.getOldPassword(),
                changeSelfPasswordDTO.getNewPassword(),
                changeSelfPasswordDTO.getRepeatedNewPassword(),
                etag,
                changeSelfPasswordDTO.getVersion()
        );
        return Response.noContent().build();
    }

    @PATCH
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{username}/password")
    @RolesAllowed({Roles.ADMIN})
    public Response changeUserPassword(@NotNull @Valid ChangeUserPasswordDTO changeUserPasswordDTO,
                                       @NotBlank @PathParam("username") String username) {
        final String etag = routingContext.request().getHeader("If-Match");

        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.changeUserPassword(username, changeUserPasswordDTO.getNewPassword(),
                        changeUserPasswordDTO.getRepeatedNewPassword(), etag, changeUserPasswordDTO.getVersion());
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        accountService.changeUserPassword(username, changeUserPasswordDTO.getNewPassword(),
                changeUserPasswordDTO.getRepeatedNewPassword(), etag, changeUserPasswordDTO.getVersion());
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
    public Response resetPasswordFromEmail(@NotNull @Valid ResetPasswordFromEmailDTO resetPasswordTokenDTO) {
        accountService.changePasswordFromResetPasswordLink(resetPasswordTokenDTO.getResetPasswordToken(),
                resetPasswordTokenDTO.getNewPassword(), resetPasswordTokenDTO.getRepeatedNewPassword());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @EtagValidator
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public Response editPersonalData(@NotNull @Valid EditPersonalDataDTO editPersonalDataDTO) {
        final String etag = routingContext.request().getHeader("If-Match");
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.editSelfPersonalData(
                        securityIdentity.getPrincipal().getName(),
                        editPersonalDataDTO.getFirstName(),
                        editPersonalDataDTO.getSurname(),
                        etag,
                        editPersonalDataDTO.getVersion()
                );
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        accountService.editSelfPersonalData(
                securityIdentity.getPrincipal().getName(),
                editPersonalDataDTO.getFirstName(),
                editPersonalDataDTO.getSurname(),
                etag,
                editPersonalDataDTO.getVersion()
        );
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/self/language")
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public Response changeLanguage(@NotNull @Valid ChangeLanguageDTO changeLanguageDTO) {
        final String etag = routingContext.request().getHeader("If-Match");
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.changeLanguage(
                        securityIdentity.getPrincipal().getName(),
                        changeLanguageDTO.getLanguage(),
                        etag,
                        changeLanguageDTO.getVersion()
                );
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        accountService.changeLanguage(
                securityIdentity.getPrincipal().getName(),
                changeLanguageDTO.getLanguage(),
                etag,
                changeLanguageDTO.getVersion()
        );
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/self/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.OWNER, Roles.MANAGER})
    public Response getSelfPersonalData() {
        PersonalData personalData = accountService.getSelfPersonalData(securityIdentity.getPrincipal().getName());
        PersonalDataDTO personalDataDTO = new PersonalDataDTO(personalData.getId().getId(), personalData.getVersion(),
                personalData.getFirstName(), personalData.getSurname());
        return Response.status(Response.Status.OK)
                .entity(personalDataDTO)
                .header("ETag", messageSigner.sign(personalDataDTO))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{username}/personal-data")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response getUserPersonalData(@PathParam("username") String username) {
        PersonalData personalData = accountService.getUserPersonalData(username);
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
                                         @PathParam("username") String username) {
        final String etag = routingContext.request().getHeader("If-Match");
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.editUserPersonalData(
                        securityIdentity.getPrincipal().getName(),
                        username,
                        editPersonalDataDTO.getFirstName(),
                        editPersonalDataDTO.getSurname(),
                        etag,
                        editPersonalDataDTO.getVersion()
                );
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }

        accountService.editUserPersonalData(
                securityIdentity.getPrincipal().getName(),
                username,
                editPersonalDataDTO.getFirstName(),
                editPersonalDataDTO.getSurname(),
                etag, editPersonalDataDTO.getVersion()
        );

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @Path("/{username}/disable")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response disableUserAccount(@PathParam("username") String username) {
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.disableUserAccount(securityIdentity.getPrincipal().getName(), username);
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
            } catch (OptimisticLockAppException | TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }

        accountService.disableUserAccount(securityIdentity.getPrincipal().getName(), username);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @Path("/{username}/enable")
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response enableUserAccount(@PathParam("username") String username) {
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.enableUserAccount(securityIdentity.getPrincipal().getName(), username);
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
            } catch (OptimisticLockAppException | TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }

        accountService.enableUserAccount(securityIdentity.getPrincipal().getName(), username);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @EtagValidator
    @Path("/add-access-level-manager")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Response addAccessLevelManager(@NotNull @Valid AddAccessLevelManagerDTO addAccessLevelManagerDTO) {
        final String etag = routingContext.request().getHeader("If-Match");
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.addAccessLevelManager(
                        securityIdentity.getPrincipal().getName(),
                        addAccessLevelManagerDTO.getUsername(),
                        addAccessLevelManagerDTO.getLicense(),
                        etag,
                        addAccessLevelManagerDTO.getVersion()
                );
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        accountService.addAccessLevelManager(
                securityIdentity.getPrincipal().getName(),
                addAccessLevelManagerDTO.getUsername(),
                addAccessLevelManagerDTO.getLicense(),
                etag,
                addAccessLevelManagerDTO.getVersion()
        );

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @EtagValidator
    @Path("/add-access-level-owner")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Response addAccessLevelOwner(@NotNull @Valid AddAccessLevelOwnerDTO addAccessLevelOwnerDTO) {
        final String etag = routingContext.request().getHeader("If-Match");

        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.addAccessLevelOwner(
                        securityIdentity.getPrincipal().getName(),
                        addAccessLevelOwnerDTO.getUsername(),
                        addAccessLevelOwnerDTO.getPhoneNumber(),
                        etag,
                        addAccessLevelOwnerDTO.getVersion()
                );
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        accountService.addAccessLevelOwner(
                securityIdentity.getPrincipal().getName(),
                addAccessLevelOwnerDTO.getUsername(),
                addAccessLevelOwnerDTO.getPhoneNumber(),
                etag,
                addAccessLevelOwnerDTO.getVersion()
        );

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @EtagValidator
    @Path("/add-access-level-admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Response addAccessLevelAdmin(@NotNull @Valid AddAccessLevelAdminDTO addAccessLevelAdminDTO) {
        final String etag = routingContext.request().getHeader("If-Match");
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.addAccessLevelAdmin(
                        securityIdentity.getPrincipal().getName(),
                        addAccessLevelAdminDTO.getUsername(),
                        etag,
                        addAccessLevelAdminDTO.getVersion()
                );
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        accountService.addAccessLevelAdmin(
                securityIdentity.getPrincipal().getName(),
                addAccessLevelAdminDTO.getUsername(),
                etag,
                addAccessLevelAdminDTO.getVersion()
        );

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @EtagValidator
    @Path("/revoke-access-level")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.ADMIN)
    public Response revokeAccessLevel(@NotNull @Valid RevokeAccessLevelDTO revokeAccessLevelDTO) {
        final String etag = routingContext.request().getHeader("If-Match");
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.revokeAccessLevel(
                        securityIdentity.getPrincipal().getName(),
                        revokeAccessLevelDTO.getUsername(),
                        revokeAccessLevelDTO.getAccessLevel(),
                        etag,
                        revokeAccessLevelDTO.getVersion()
                );

                rollbackTX = accountService.isLastTransactionRollback();

                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }

        accountService.revokeAccessLevel(
                securityIdentity.getPrincipal().getName(),
                revokeAccessLevelDTO.getUsername(),
                revokeAccessLevelDTO.getAccessLevel(),
                etag,
                revokeAccessLevelDTO.getVersion()
        );

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER})
    public Response getListOfAccounts(@DefaultValue("username") @QueryParam("sortBy") String sortBy,
                                      @DefaultValue("0") @QueryParam("pageNumber") int pageNumber,
                                      @DefaultValue("10") @QueryParam("pageSize") int pageSize,
                                      @QueryParam("isEnable") Boolean isEnable) {
        final List<AccountForListDTO> listOfAccounts = accountService.getListOfAccounts(sortBy, pageNumber, pageSize, isEnable)
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

    @GET
    @Path("/self")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.ADMIN, Roles.MANAGER, Roles.OWNER})
    public Response getSelfAccount() {
        Account account = accountService.getSelfAccount(securityIdentity.getPrincipal().getName());
        final PersonalData personalData = accountService.getUserPersonalData(securityIdentity.getPrincipal().getName());
        account.setPersonalData(personalData);
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
                                    @PathParam("username") String username) {
        final String etag = routingContext.request().getHeader("If-Match");
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.changeUserEmail(
                        securityIdentity.getPrincipal().getName(),
                        changeEmailDTO.getNewEmail(),
                        username,
                        etag,
                        changeEmailDTO.getVersion()
                );

                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        accountService.changeUserEmail(
                securityIdentity.getPrincipal().getName(),
                changeEmailDTO.getNewEmail(),
                username,
                etag,
                changeEmailDTO.getVersion()
        );

        return Response.noContent().build();
    }

    @GET
    @Path("/self/owner")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.OWNER)
    public Response getMyOwnerAccount() {
        final Owner owner = accountService.getOwner(securityIdentity.getPrincipal().getName());
        final OwnerDTO ownerDTO = AccountMapper.createOwnerDTOEntity(owner,
                accountService.getUserPersonalData(owner.getAccount().getUsername()));
        final OwnerETagDTO ownerETagDTO = new OwnerETagDTO(owner.getId(), owner.getVersion(), owner.getPhoneNumber());
        return Response.ok()
                .entity(ownerDTO)
                .header("ETag", messageSigner.sign(ownerETagDTO))
                .build();
    }

    @GET
    @Path("/self/manager")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.MANAGER)
    public Response getMyManagerAccount() {
        final Manager manager = accountService.getManager(securityIdentity.getPrincipal().getName());
        final ManagerDTO managerDTO = AccountMapper.createManagerDTOEntity(manager,
                accountService.getUserPersonalData(manager.getAccount().getUsername()));
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
        final Admin admin = accountService.getAdmin(securityIdentity.getPrincipal().getName());
        final AdminDTO adminDTO = AccountMapper.createAdminDTOEntity(admin,
                accountService.getUserPersonalData(admin.getAccount().getUsername()));
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
    public Response changeSelfEmail(@NotNull @Valid ChangeEmailDTO changeEmailDTO) {
        final String etag = routingContext.request().getHeader("If-Match");
        int retryTXCounter = txRetries;
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                accountService.changeSelfEmail(
                        securityIdentity.getPrincipal().getName(),
                        changeEmailDTO.getNewEmail(),
                        etag,
                        changeEmailDTO.getVersion()
                );
                rollbackTX = accountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (TransactionRollbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        accountService.changeSelfEmail(
                securityIdentity.getPrincipal().getName(),
                changeEmailDTO.getNewEmail(),
                etag,
                changeEmailDTO.getVersion()
        );
        return Response.noContent().build();
    }

    @PATCH
    @Path("/self/confirm-new-email")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.GUEST, Roles.OWNER, Roles.MANAGER, Roles.ADMIN})
    public Response confirmNewEmail(@NotNull @Valid TokenFromEmailDTO activationTokenDTO) {
        accountService.confirmNewEmailAccountFromActivationLink(activationTokenDTO.getActivationToken());
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}