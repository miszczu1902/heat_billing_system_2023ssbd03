package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

public class AdminDTO extends AccountDTO {

    public String firstName;

    public String surname;

    public String language;

    public AdminDTO(String email, String username, String firstName, String surname, String language) {
        super(email, username);
        this.firstName = firstName;
        this.surname = surname;
        this.language = language;
    }
}
