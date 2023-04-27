package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    private String username;
    private String password;
}
