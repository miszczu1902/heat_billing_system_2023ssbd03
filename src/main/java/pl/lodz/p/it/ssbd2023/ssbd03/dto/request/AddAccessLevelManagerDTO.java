package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAccessLevelManagerDTO extends VersionDTO {
    @Size(min = 6, max = 16,
            message = "Max length for username is between 6 - 16 ")
    private String username;
    @Length(min = 20, max = 20,
            message = "License length must be 20 characters")
    private String license;
}
