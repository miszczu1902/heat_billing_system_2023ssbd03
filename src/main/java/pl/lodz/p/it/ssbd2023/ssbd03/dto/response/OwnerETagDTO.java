package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OwnerETagDTO extends AbstractDTO implements Serializable, Signable {
    private String phoneNumber;

    public OwnerETagDTO(Long id, Long version, String phoneNumber) {
        super(id, version);
        this.phoneNumber = phoneNumber;
    }
    @Override
    public String messageToSign() {
        return phoneNumber
                .concat(getId().toString())
                .concat(getVersion().toString());
    }
}
