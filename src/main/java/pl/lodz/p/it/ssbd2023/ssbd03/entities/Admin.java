package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.io.Serializable;

@NoArgsConstructor
@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "admin")
public class Admin extends AccessLevelMapping implements Serializable, Signable {

    @Override
    public String messageToSign() {
        return super.getAccount().messageToSign()
                .concat(getId().toString())
                .concat(getVersion().toString());
    }
}
