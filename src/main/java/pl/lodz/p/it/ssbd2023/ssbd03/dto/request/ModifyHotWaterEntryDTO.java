package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.DecimalMin;
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
    @DecimalMin(value = "0")
    private BigDecimal hotWaterConsumption;

    private Long placeId;

    @Override
    public String messageToSign() {
        return getHotWaterConsumption().toString()
                .concat(getPlaceId().toString());
    }
}
