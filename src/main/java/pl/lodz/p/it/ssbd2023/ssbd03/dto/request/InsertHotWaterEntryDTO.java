package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertHotWaterEntryDTO {
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal hotWaterConsumption;

    @NotNull
    private Long placeId;
}