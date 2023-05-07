package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevokeAccessLevelDTO {
    @Size(min = 6, max = 16,
            message = "Max length for username is between 6 - 16 ")
    private String username;
    @Pattern(regexp = "^(ADMIN|MANAGER|OWNER)$",
            message = "Access level must be ADMIN, MANAGER or OWNER")
    private String accessLevel;
}
