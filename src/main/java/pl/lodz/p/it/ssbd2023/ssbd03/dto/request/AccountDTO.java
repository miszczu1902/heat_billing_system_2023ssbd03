package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO extends AbstractDTO {
    private String email;
    private String username;
    private String password;
    private String repeatedPassword;

    public AccountDTO(Long id, Long version, String email, String username) {
        super(id, version);
        this.email = email;
        this.username = username;
    }

    public AccountDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
