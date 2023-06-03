package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddConsumptionDTO {

    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 8, fraction = 2, message = "value can have 8 digits before the decimal point and max 2 digits after the decimal point")
    BigDecimal consumption;
    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 8, fraction = 2, message = "value can have 8 digits before the decimal point and max 2 digits after the decimal point")
    BigDecimal consumptionCost;
    @NotNull
    @Digits(integer = 1, fraction = 2, message = "value can have 1 digit before the decimal point and max 2 digits after the decimal point")
    @DecimalMin(value = "0.0", message = "value can be between 0,1")
    @DecimalMax(value = "1.0", message = "value can be between 0,1")
    BigDecimal heatingAreaFactor;
}
