package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ModifyHotWaterEntryDTO extends VersionDTO implements Serializable, Signable {
    @DecimalMin(value = "0.01")
    private BigDecimal hotWaterConsumption;

    private Long placeId;

    public ModifyHotWaterEntryDTO(@NotNull Long version, BigDecimal hotWaterConsumption, Long placeId) {
        super(version);
        this.hotWaterConsumption = hotWaterConsumption;
        this.placeId = placeId;
    }

    @Override
    public String messageToSign() {
        return getHotWaterConsumption().toString()
                .concat(getPlaceId().toString());
    }
}
