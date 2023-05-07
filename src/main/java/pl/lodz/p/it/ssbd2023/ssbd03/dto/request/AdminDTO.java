package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import lombok.Data;

@Data
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
