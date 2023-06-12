package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HeatDistributionCentrePayoffDTO extends AbstractDTO {
    private LocalDate date;
    private Long heatDistributionCentreId;
    private String manager;
    private BigDecimal heatingAreaFactor;
    private BigDecimal consumptionCost;

    public HeatDistributionCentrePayoffDTO(Long id, Long version, LocalDate date, Long heatDistributionCentreId, String manager, BigDecimal heatingAreaFactor, BigDecimal consumptionCost) {
        super(id, version);
        this.date = date;
        this.heatDistributionCentreId = heatDistributionCentreId;
        this.manager = manager;
        this.heatingAreaFactor = heatingAreaFactor;
        this.consumptionCost = consumptionCost;
    }
}
