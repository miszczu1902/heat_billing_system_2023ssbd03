package pl.lodz.p.it.ssbd2023.ssbd03.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CreateOwnerDTO implements Serializable {
    private String firstname;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String language;
    private String phoneNumber;
}
