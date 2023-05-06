package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetAccountForListDTO extends AbstractDTO {
    private String email;
    private String username;
    private List<String> accessLevels;

    public GetAccountForListDTO(Long id, Long version, String email, String username, List<String> accessLevels) {
        super(id, version);
        this.email = email;
        this.username = username;
        this.accessLevels = accessLevels;
    }
}
