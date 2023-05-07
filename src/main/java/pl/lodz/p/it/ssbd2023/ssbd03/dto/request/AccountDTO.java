package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO extends AbstractDTO {
    private String email;
    private String username;
    private String password;
    private String repeatedPassword;

    public AccountDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
