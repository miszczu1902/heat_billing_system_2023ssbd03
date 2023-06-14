package pl.lodz.p.it.ssbd2023.ssbd03.mow.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.UnitWarmCostReportDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatingPlaceAndCommunalAreaAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.BalanceService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.AdvanceMapper;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.BalanceMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Path("/balances")
@RequestScoped
public class BalanceEndpoint {
    @Inject
    private BalanceService balanceService;

    @Inject
    MessageSigner messageSigner;

    protected static final Logger LOGGER = Logger.getGlobal();

    //MOW 1
    @GET
    @Path("/unit-cost-report")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.GUEST, Roles.MANAGER, Roles.OWNER})
    public Response getUnitWarmCostReport() {
        UnitWarmCostReportDTO unitWarmCostReportDTO = new UnitWarmCostReportDTO(balanceService.getUnitWarmCostReportHotWater(), balanceService.getUnitWarmCostReportCentralHeating(),
                LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        return Response.ok().entity(unitWarmCostReportDTO).build();
    }

    //MOW 2
    @Path("/self/report/{placeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfReport(@NotBlank @PathParam("placeId") String placeId) {
        return Response.ok().entity(balanceService.getSelfReport(placeId)).build();
    }

    //MOW 2
    @Path("/report/{placeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getUserReport(@NotBlank @PathParam("placeId") String placeId) {
        return Response.ok().entity(balanceService.getUserReport(placeId)).build();
    }

    //MOW 3
    @Path("/all-reports/{buildingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getAllReports(@DefaultValue("0") @QueryParam("pageNumber") int pageNumber,
                                  @DefaultValue("10") @QueryParam("pageSize") int pageSize,
                                  @PathParam("buildingId") Long buildingId) {
        return Response.ok().entity(balanceService.getAllReports(pageNumber, pageSize, buildingId).stream()
                .map(BalanceMapper::balancesToAnnualBalancesForListDTO)
                .toList()).build();
    }

    //MOW 9
    @Path("/self/all-reports")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfReports(@DefaultValue("0") @QueryParam("pageNumber") int pageNumber,
                                   @DefaultValue("10") @QueryParam("pageSize") int pageSize) {
        return Response.ok().entity(balanceService.getSelfReports(pageNumber, pageSize).stream()
                .map(BalanceMapper::balancesToAnnualBalancesForListDTO)
                .toList()).build();
    }

    //MOW 7
    @Path("/self/{placeId}/advances-values/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfAdvancesValues(@PathParam("placeId") Long placeId, @PathParam("year") Integer year) {
        List<HotWaterAdvance> hotWaterAdvancesValues = balanceService.getSelfWaterAdvanceValue(placeId, year);
        List<HeatingPlaceAndCommunalAreaAdvance> heatingPlaceAndCommunalAreaAdvancesValues = balanceService.getSelfHeatingAdvanceValue(placeId, year);
        return Response.ok()
                .entity(AdvanceMapper.createListOfAdvanceForMonthDTOFromListOfAdvances(hotWaterAdvancesValues, heatingPlaceAndCommunalAreaAdvancesValues))
                .build();
    }

    //MOW 7
    @Path("/{placeId}/advances-values/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getAdvancesValuesForPlace(@PathParam("placeId") Long placeId, @PathParam("year") Integer year) {
        List<HotWaterAdvance> hotWaterAdvancesValues = balanceService.getUserWaterAdvanceValue(placeId, year);
        List<HeatingPlaceAndCommunalAreaAdvance> heatingPlaceAndCommunalAreaAdvancesValues = balanceService.getUserHeatingAdvanceValue(placeId, year);
        return Response.ok()
                .entity(AdvanceMapper.createListOfAdvanceForMonthDTOFromListOfAdvances(hotWaterAdvancesValues, heatingPlaceAndCommunalAreaAdvancesValues))
                .build();
    }

    //MOW8
    @Path("self/cost-balance")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.OWNER})
    public Response getSelfHeatingBalance() {
        return Response.ok().entity(balanceService.getSelfHeatingBalance()).build();
    }

    //MOW 8
    @Path("{user}/cost-balance")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed({Roles.MANAGER})
    public Response getUserHeatingBalance() {
        return Response.ok().entity(balanceService.getUserHeatingBalance()).build();
    }
}