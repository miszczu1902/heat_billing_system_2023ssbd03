package pl.lodz.p.it.ssbd2023.ssbd03.mow.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.AddConsumptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.InsertAdvanceChangeFactorDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.InsertHotWaterEntryDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ModifyHotWaterEntryDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.HotWaterEntryDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.AppException;
import pl.lodz.p.it.ssbd2023.ssbd03.exceptions.database.OptimisticLockAppException;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.HeatDistributionCentreService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.LoadConfig;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.HotWaterEntryMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/heat-distribution-centre")
@RequestScoped
public class HeatDistributionCentreEndpoint {

    @Inject
    private HeatDistributionCentreService heatDistributionCentreService;

    private int txRetries = Integer.parseInt(LoadConfig.loadPropertyFromConfig("tx.retries"));

    @Inject
    MessageSigner messageSigner;

    protected static final Logger LOGGER = Logger.getGlobal();

    //MOW 4
    @Path("/parameters")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getHeatDistributionCentreParameters() {
        return Response.status(200).entity(heatDistributionCentreService.getHeatDistributionCentreParameters()).build();
    }

    //MOW 12
    @PATCH
    @Path("/parameters/advance-change-factor/{buildingId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Roles.MANAGER)
    public Response insertAdvanceChangeFactor(@PathParam("buildingId") Long buildingId,  InsertAdvanceChangeFactorDTO insertAdvanceChangeFactorDTO) {
        heatDistributionCentreService.insertAdvanceChangeFactor(
                insertAdvanceChangeFactorDTO.getAdvanceChangeFactor(), buildingId);
        return Response.status(204).build();
    }

    //MOW 13
    @Path("/parameters/insert-consumption")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public Response insertConsumption(InsertHotWaterEntryDTO hotWaterEntryDTO) {
        heatDistributionCentreService.insertConsumption(hotWaterEntryDTO.getHotWaterConsumption(), hotWaterEntryDTO.getPlaceId());
        return Response.status(204).build();
    }

    //MOW 13
    @Path("/parameters/insert-consumption")
    @Produces(MediaType.APPLICATION_JSON)
    @PATCH
    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public Response modifyConsumption(@NotNull @Valid ModifyHotWaterEntryDTO hotWaterEntryDTO, @Context HttpServletRequest request) {
        final String etag = request.getHeader("If-Match");
        int retryTXCounter = txRetries; //limit prób ponowienia transakcji
        boolean rollbackTX = false;

        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                heatDistributionCentreService.modifyConsumption(
                        hotWaterEntryDTO.getHotWaterConsumption(),
                        hotWaterEntryDTO.getPlaceId(),
                        hotWaterEntryDTO.getVersion(),
                        etag);
                rollbackTX = heatDistributionCentreService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
                else return Response.status(Response.Status.NO_CONTENT).build();
            } catch (EJBTransactionRolledbackException | OptimisticLockAppException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        heatDistributionCentreService.modifyConsumption(
                hotWaterEntryDTO.getHotWaterConsumption(),
                hotWaterEntryDTO.getPlaceId(),
                hotWaterEntryDTO.getVersion(),
                etag);

        return Response.status(204).build();
    }


    //MOW 13
    @GET
    @Path("/hot-water-consumption/{hotWaterEntryId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.OWNER, Roles.MANAGER})
    public Response getHotWaterEntry(@PathParam("hotWaterEntryId") Long hotWaterEntryId) {
        HotWaterEntryDTO hotWaterEntry = HotWaterEntryMapper.createHotWaterEntryToHotWaterEntryDTO(
                heatDistributionCentreService.getHotWaterEntry(hotWaterEntryId));
        return Response.status(200)
                .header("ETag", messageSigner.sign(hotWaterEntry))
                .entity(hotWaterEntry)
                .build();
    }

    //MOW 14,15,16
    @Path("/parameters/consumption-cost")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @RolesAllowed({Roles.MANAGER})
    public Response addConsumptionFromInvoice(@NotNull @Valid AddConsumptionDTO addConsumptionDTO) {
        int retryTXCounter = txRetries; //limit prób ponowienia transakcji
        boolean rollbackTX = false;
        do {
            LOGGER.log(Level.INFO, "*** Powtarzanie transakcji, krok: {0}", retryTXCounter);
            try {
                heatDistributionCentreService.addConsumptionFromInvoice(addConsumptionDTO.getConsumption(), addConsumptionDTO.getConsumptionCost(), addConsumptionDTO.getHeatingAreaFactor());

                rollbackTX = heatDistributionCentreService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** *** Odwolanie transakcji");
            } catch (OptimisticLockAppException | EJBTransactionRolledbackException ex) {
                rollbackTX = true;
                if (retryTXCounter < 2) {
                    throw ex;
                }
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw AppException.createTransactionRollbackException();
        }
        return Response.status(Response.Status.CREATED).build();
    }
}
