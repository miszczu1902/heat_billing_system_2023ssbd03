package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ManagerDTO extends AccountDTO {
    private String firstName;
    private String surname;
    private String language;
    private String license;

    public ManagerDTO(String email, String username, String firstName, String surname, String language, String license) {
        super(email, username);
        this.firstName = firstName;
        this.surname = surname;
        this.language = language;
        this.license = license;
    }

    @Override
    public String messageToSign() {
        return super.messageToSign()
                .concat(firstName)
                .concat(surname)
                .concat(language)
                .concat(license);
    }
}
