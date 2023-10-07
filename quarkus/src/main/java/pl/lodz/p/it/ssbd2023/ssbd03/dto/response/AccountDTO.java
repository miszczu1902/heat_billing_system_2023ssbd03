package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO extends AbstractDTO implements Signable {
    private String email;
    private String username;
    private String password;

    public AccountDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public AccountDTO(Long id, Long version, String email, String username) {
        this.id = id;
        this.version = version;
        this.email = email;
        this.username = username;
    }

    @Override
    public String messageToSign() {
        return email
                .concat(username)
                .concat(getId().toString())
                .concat(getVersion().toString());
    }
}
