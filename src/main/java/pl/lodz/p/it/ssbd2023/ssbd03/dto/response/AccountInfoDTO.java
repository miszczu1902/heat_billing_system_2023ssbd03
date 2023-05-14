package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountInfoDTO extends AbstractDTO implements Signable {
    private String email;
    private String username;
    private Boolean isEnable;
    private Boolean isActive;
    private String registerDate;
    private List<String> accessLevels;
    private String firstName;
    private String surname;
    private String phoneNumber;
    private String license;

    public AccountInfoDTO(Long id, Long version, String email, String username, Boolean isEnable, Boolean isActive, String registerDate, List<String> accessLevels, String firstName, String surname, String phoneNumber, String license) {
        super(id, version);
        this.email = email;
        this.username = username;
        this.isEnable = isEnable;
        this.isActive = isActive;
        this.registerDate = registerDate;
        this.accessLevels = accessLevels;
        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.license = license;
    }

    @Override
    public String messageToSign() {
        if (phoneNumber == null && license != null) {
            return email
                    .concat(username)
                    .concat(isEnable.toString())
                    .concat(isActive.toString())
                    .concat(registerDate)
                    .concat(accessLevels.stream().toString())
                    .concat(firstName)
                    .concat(surname)
                    .concat(license)
                    .concat(getId().toString())
                    .concat(getVersion().toString());
        } else if (license == null && phoneNumber != null) {
            return email
                    .concat(username)
                    .concat(isEnable.toString())
                    .concat(isActive.toString())
                    .concat(registerDate)
                    .concat(accessLevels.stream().toString())
                    .concat(firstName)
                    .concat(surname)
                    .concat(phoneNumber)
                    .concat(getId().toString())
                    .concat(getVersion().toString());
        } else if (phoneNumber == null && license == null) {
            return email
                    .concat(username)
                    .concat(isEnable.toString())
                    .concat(isActive.toString())
                    .concat(registerDate)
                    .concat(accessLevels.stream().toString())
                    .concat(firstName)
                    .concat(surname)
                    .concat(getId().toString())
                    .concat(getVersion().toString());
        }
            return email
                    .concat(username)
                    .concat(isEnable.toString())
                    .concat(isActive.toString())
                    .concat(registerDate)
                    .concat(accessLevels.stream().toString())
                    .concat(firstName)
                    .concat(surname)
                    .concat(phoneNumber)
                    .concat(license)
                    .concat(getId().toString())
                    .concat(getVersion().toString());

    }
}
