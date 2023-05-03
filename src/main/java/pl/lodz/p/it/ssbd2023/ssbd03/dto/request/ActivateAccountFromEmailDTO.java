package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActivateAccountFromEmailDTO {
    @Length(min = 10, max = 10)
    private String activationToken;
}
