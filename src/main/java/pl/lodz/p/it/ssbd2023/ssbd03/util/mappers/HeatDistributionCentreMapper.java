package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.HeatDistributionCentrePayoffDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatDistributionCentrePayoff;

public class HeatDistributionCentreMapper {
    public static HeatDistributionCentrePayoffDTO createHeatDistributionCentrePayoffToHeatDistributionCentrePayoffDTO(HeatDistributionCentrePayoff heatDistributionCentrePayoff) {
        return new HeatDistributionCentrePayoffDTO(
                heatDistributionCentrePayoff.getId(),
                heatDistributionCentrePayoff.getVersion(),
                heatDistributionCentrePayoff.getDate(),
                heatDistributionCentrePayoff.getHeatDistributionCentre().getId(),
                heatDistributionCentrePayoff.getManager().getAccount().getUsername(),
                heatDistributionCentrePayoff.getHeatingAreaFactor(),
                heatDistributionCentrePayoff.getConsumptionCost());
    }
}
