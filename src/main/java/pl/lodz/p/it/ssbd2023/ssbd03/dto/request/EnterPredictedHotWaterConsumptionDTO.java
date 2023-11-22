package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnterPredictedHotWaterConsumptionDTO extends VersionDTO implements Serializable {

    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 8, fraction = 2, message = "value can have 8 digits before the decimal point and max 2 digits after the decimal point")
    BigDecimal consumption;

    public EnterPredictedHotWaterConsumptionDTO(int version, BigDecimal consumption) {
        super(version);
        this.consumption = consumption;
    }
}
