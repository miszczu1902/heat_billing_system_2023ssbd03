package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import lombok.Data;

@Data
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
}
