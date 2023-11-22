package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HotWaterEntryDTO extends VersionDTO implements Signable {
    private Long id;
    private LocalDate date;
    private BigDecimal entryValue;
    private Long placeId;
    private String manager;

    public HotWaterEntryDTO(@NotNull Long version, Long id, LocalDate date, BigDecimal entryValue, Long placeId, String manager) {
        super(version);
        this.id = id;
        this.date = date;
        this.entryValue = entryValue;
        this.placeId = placeId;
        this.manager = manager;
    }

    public HotWaterEntryDTO(@NotNull Long version, Long id, LocalDate date, BigDecimal entryValue, Long placeId) {
        super(version);
        this.id = id;
        this.date = date;
        this.entryValue = entryValue;
        this.placeId = placeId;
    }

    @Override
    public String messageToSign() {
        String message = getVersion().toString()
                .concat(getDate().toString())
                .concat(getEntryValue().toString());
        if (getManager() != null) {
            message = message.concat(getManager());
        }
        return message;
    }
}
