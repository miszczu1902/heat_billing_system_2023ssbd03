package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;


public class OwnerDTO extends AccountDTO {

    public String firstName;
    public String surname;
    public String language;
    public String phoneNumber;

    public OwnerDTO(String email, String username, String firstName, String surname, String language, String phoneNumber) {
        super(email, username);
        this.firstName = firstName;
        this.surname = surname;
        this.language = language;
        this.phoneNumber = phoneNumber;
    }
}
