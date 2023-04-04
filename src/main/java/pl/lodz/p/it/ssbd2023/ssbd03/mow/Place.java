package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Place {

    private Integer id;

    @Setter
    private BigDecimal area;

    @Setter
    private Boolean hotWaterConnection;

    @Setter
    private Boolean centralHeatingConnection;

    @Setter
    private BigDecimal predictedHotWaterConsumption;

    @Setter
    private List<Advance> advances;

}
