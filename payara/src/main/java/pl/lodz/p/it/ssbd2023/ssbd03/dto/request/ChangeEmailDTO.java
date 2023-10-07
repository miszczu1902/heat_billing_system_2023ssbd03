package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeEmailDTO extends VersionDTO implements Serializable {
    @NotNull
    @Size(max = 255, message = "Max length for email is 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,10}$",
            message = "Email should contains: \"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{1,10}$\"")
    private String newEmail;
}
