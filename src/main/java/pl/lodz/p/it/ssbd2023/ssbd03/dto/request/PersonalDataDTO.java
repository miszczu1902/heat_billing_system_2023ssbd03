package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDataDTO implements Serializable {
    @Size(min = 6, max = 16,
            message = "Max length for username is between 6 - 16 ")
    private String username;
    @NotNull
    @Size(max = 32, message = "Max length for first name is 32")
    private String firstName;
    @NotNull
    @Size(max = 32, message = "Max length for surname is 32")
    private String surname;
}