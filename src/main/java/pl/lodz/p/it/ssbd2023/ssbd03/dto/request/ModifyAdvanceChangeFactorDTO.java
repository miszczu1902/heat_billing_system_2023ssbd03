package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyAdvanceChangeFactorDTO {
    @DecimalMin(value = "0")
    @DecimalMax(value = "9")
    private BigDecimal advanceChangeFactor;

    private Long placeId;
}
