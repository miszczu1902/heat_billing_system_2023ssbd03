package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneNumberDTO {
    @Length(min = 9, max = 9,
            message = "The lenght of the phone number must be 9")
    private String phoneNumber;
}
