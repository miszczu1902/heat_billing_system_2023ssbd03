package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractVersionDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneNumberDTOAbstract extends AbstractVersionDTO {
    @Length(min = 9, max = 9,
            message = "The length of the phone number must be 9")
    private String phoneNumber;
}
