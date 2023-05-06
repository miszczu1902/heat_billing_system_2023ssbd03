package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAccessLevelManagerDTO {
    @Size(min = 6, max = 16,
        message = "Max length for username is between 6 - 16 ")
    private String username;
    @Length(max = 20)
    private String license;
}
