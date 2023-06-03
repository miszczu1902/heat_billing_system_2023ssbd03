package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddConsumptionDTO {

    @NotNull
    BigDecimal consumption;
    @NotNull
    BigDecimal consumptionCost;
    @NotNull
    @DecimalMin(value= "0.0",message = "value can be between 0,1")
    @DecimalMax(value = "1.0",message = "value can be between 0,1")
    BigDecimal heatingAreaFactor;
}
