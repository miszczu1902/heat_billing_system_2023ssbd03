package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAccessLevelOwnerDTO {
    @Size(min = 6, max = 16,
            message = "Max length for username is between 6 - 16 ")
    private String username;
    @Length(min = 9, max = 9,
            message = "Phone number length must be 9 digits")
    private String phoneNumber;
}
