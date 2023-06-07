package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ModifyHotWaterEntryDTO extends VersionDTO {
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal hotWaterConsumption;

    @NotNull
    private Long placeId;

    public ModifyHotWaterEntryDTO(@NotNull Long version, @NotNull BigDecimal hotWaterConsumption, @NotNull Long placeId) {
        super(version);
        this.hotWaterConsumption = hotWaterConsumption;
        this.placeId = placeId;
    }
}
