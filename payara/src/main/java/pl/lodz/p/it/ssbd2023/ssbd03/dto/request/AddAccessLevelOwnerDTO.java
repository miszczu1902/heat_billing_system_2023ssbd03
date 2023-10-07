package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAccessLevelOwnerDTO extends VersionDTO {
    @Size(min = 6, max = 16,
            message = "Max length for username is between 6 - 16 ")
    private String username;
    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must consist of exactly 9 digits")
    private String phoneNumber;
}
