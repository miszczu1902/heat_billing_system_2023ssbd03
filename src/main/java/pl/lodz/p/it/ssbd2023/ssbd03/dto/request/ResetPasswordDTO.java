package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {
    @NotBlank
    @Size(min = 6, max = 16,
            message = "Max length for username is between 6 - 16 ")
    @Pattern(regexp = "^[a-zA-Z0-9_]{6,16}$", message = "Username can only contain letters, numbers, digits, and underscore")
    private String username;
}
