package pl.lodz.p.it.ssbd2023.ssbd03.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionDTO {
    @NotNull
    protected Long version;

    public VersionDTO(int version) {
        this.version = (long) version;
    }
}
