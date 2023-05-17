package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPersonalDataDTO extends VersionDTO implements Serializable, Signable {
    @NotNull
    @Size(max = 32, message = "Max length for first name is 32")
    private String firstName;
    @NotNull
    @Size(max = 32, message = "Max length for surname is 32")
    private String surname;

    public EditPersonalDataDTO(int version, String firstName, String surname) {
        super(version);
        this.firstName = firstName;
        this.surname = surname;
    }

    @Override
    public String messageToSign() {
        return firstName.concat(surname);
    }
}