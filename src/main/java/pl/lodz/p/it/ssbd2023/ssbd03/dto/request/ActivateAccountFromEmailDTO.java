package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActivateAccountFromEmailDTO {
    private String activationToken;
}
