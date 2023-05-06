package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountForListDTO extends AbstractDTO {
    private String email;
    private String username;

    public AccountForListDTO(Long id, Long version, String email, String username) {
        super(id, version);
        this.email = email;
        this.username = username;
    }
}
