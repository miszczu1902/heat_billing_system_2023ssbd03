package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ModifyPlaceOwnerDTO extends VersionDTO implements Serializable {
    @NotNull
    private String username;

    public ModifyPlaceOwnerDTO(@NotNull Long version, String username) {
        super(version);
        this.username = username;
    }
}

