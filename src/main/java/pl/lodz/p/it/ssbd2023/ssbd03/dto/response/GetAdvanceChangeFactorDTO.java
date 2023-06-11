package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetAdvanceChangeFactorDTO extends VersionDTO implements Signable {
    private BigDecimal advanceChangeFactor;

    public GetAdvanceChangeFactorDTO(@NotNull Long version, BigDecimal advanceChangeFactor) {
        super(version);
        this.advanceChangeFactor = advanceChangeFactor;
    }

    @Override
    public String messageToSign() {
        return getVersion().toString()
                .concat(getAdvanceChangeFactor().toString());
    }
}
