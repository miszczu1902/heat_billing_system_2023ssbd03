package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminDTO extends AccountDTO {
    private String firstName;
    private String surname;
    private String language;

    public AdminDTO(String email, String username, String firstName, String surname, String language) {
        super(email, username);
        this.firstName = firstName;
        this.surname = surname;
        this.language = language;
    }
}
