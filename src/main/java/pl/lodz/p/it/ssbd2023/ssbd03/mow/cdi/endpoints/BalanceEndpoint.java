package pl.lodz.p.it.ssbd2023.ssbd03.mow.cdi.endpoints;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.UnitWarmCostReportDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatingPlaceAndCommunalAreaAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.BalanceService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.AdvanceMapper;
import pl.lodz.p.it.ssbd2023.ssbd03.util.mappers.BalanceMapper;

import java.time.LocalDate;
import java.util.List;

@Path("/balances")
@RequestScoped
public class BalanceEndpoint {
    @Inject
    private BalanceService balanceService;

    @GET
    @Path("/unit-cost-report")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.GUEST, Roles.MANAGER, Roles.OWNER})
    public Response getUnitWarmCostReport() {
        UnitWarmCostReportDTO unitWarmCostReportDTO = new UnitWarmCostReportDTO(balanceService.getUnitWarmCostReportHotWater(), balanceService.getUnitWarmCostReportCentralHeating(),
                LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        return Response.ok().entity(unitWarmCostReportDTO).build();
    }


    @Path("/report/{reportId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed(Roles.MANAGER)
    public Response getUserReport(@PathParam("reportId") Long reportId) {
        return Response.ok().entity(
                BalanceMapper.createYearReportDTOFromAnnualBalance(
                balanceService.getYearReport(reportId))).build();
    }

    @Path("/owner/report/{reportId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed(Roles.OWNER)
    public Response getOwnerUserReport(@PathParam("reportId") Long reportId) {
        return Response.ok().entity(
                BalanceMapper.createYearReportDTOFromAnnualBalance(
                        balanceService.getYearReport(reportId))).build();
    }

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
}