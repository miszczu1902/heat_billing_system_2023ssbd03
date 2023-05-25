package pl.lodz.p.it.ssbd2023.ssbd03.mow.cdi.endpoints;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd03.config.Roles;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.request.ResetPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.ejb.services.HeatDistributionCentreService;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.MessageSigner;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Path("/heat-distribution-centre")
@RequestScoped
public class HeatDistributionCentreEndpoint {

    @Inject
    private HeatDistributionCentreService heatDistributionCentreService;

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
    @Path("/parameters/heating-area-factor")
    @Produces(MediaType.APPLICATION_JSON)
    @PATCH
    @RolesAllowed({Roles.MANAGER})
    public Response modifyHeatingAreaFactor(BigDecimal heatingAreaFactorValue) {
        heatDistributionCentreService.modifyHeatingAreaFactor(heatingAreaFactorValue);
        return Response.status(200).build();
    }

    //MOW 13
    @Path("/parameters/consumption")
    @Produces(MediaType.APPLICATION_JSON)
    @PATCH
    @RolesAllowed({Roles.MANAGER})
    public Response modifyConsumption(BigDecimal consumptionValue) {
        heatDistributionCentreService.modifyConsumption(consumptionValue);
        return Response.status(200).build();
    }

    //MOW 14
    @Path("/parameters/consumption-cost")
    @Produces(MediaType.APPLICATION_JSON)
    @PATCH
    @RolesAllowed({Roles.MANAGER})
    public Response modifyConsumptionCost(BigDecimal consumptionCostValue) {
        heatDistributionCentreService.modifyConsumptionCost(consumptionCostValue);
        return Response.status(200).build();
    }

    //MOW 15
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add-factor")
    @RolesAllowed({Roles.MANAGER})
    public Response addHeatingAreaFactor(BigDecimal heatingAreaFactor) {
        heatDistributionCentreService.addHeatingAreaFactor(heatingAreaFactor);
        return Response.noContent().build();
    }

}
