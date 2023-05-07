package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

public class ManagerDTO extends AccountDTO {
    public String firstName;
    public String surname;
    public String language;
    public String license;

    public ManagerDTO(String email, String username, String firstName, String surname, String language, String license) {
        super(email, username);
        this.firstName = firstName;
        this.surname = surname;
        this.language = language;
        this.license = license;
    }
}
