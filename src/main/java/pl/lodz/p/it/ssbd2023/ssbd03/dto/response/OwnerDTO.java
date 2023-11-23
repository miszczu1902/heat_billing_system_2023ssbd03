package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OwnerDTO extends AccountDTO {
    private String firstName;
    private String surname;
    private String language;
    private String phoneNumber;

    public OwnerDTO(String email, String username, String firstName, String surname, String language, String phoneNumber) {
        super(email, username);
        this.firstName = firstName;
        this.surname = surname;
        this.language = language;
        this.phoneNumber = phoneNumber;
    }

    public OwnerDTO(Long id, Long version, String email, String username, String firstName, String surname, String language, String phoneNumber) {
        super(id, version, email, username);
        this.firstName = firstName;
        this.surname = surname;
        this.language = language;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String messageToSign() {
        return super.messageToSign()
                .concat(firstName)
                .concat(surname)
                .concat(language)
                .concat(phoneNumber);
    }
}
