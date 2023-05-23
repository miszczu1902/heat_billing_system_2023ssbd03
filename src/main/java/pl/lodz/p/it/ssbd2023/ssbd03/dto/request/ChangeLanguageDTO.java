package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLanguageDTO extends VersionDTO implements Serializable {
    @Pattern(regexp = "^(PL|EN)$",
            message = "Language can be: EN, PL")
    private String language;
}
