package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOwnerDTO implements Serializable {
    @NotNull
    @Size(max = 32, message = "Max length for first name is 32")
    private String firstName;

    @NotNull
    @Size(max = 32, message = "Max length for surname is 32")
    private String surname;
    @NotNull
    @Size(min = 6, max = 16,
            message = "Max length for username is between 6 - 16 ")
    @Pattern(regexp = "^[a-zA-Z0-9_]{6,}$", message = "Username can only contain letters, numbers, digits, and underscore")
    private String username;

    @NotNull
    @Size(max = 255, message = "Max length for email is 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,10}$",
            message = "Email should contains: \"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{1,10}$\"")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$",
    message = "Restrictions for password is: between 8-32 characters length, at least one upper and lower case, number and special digit")
    @ToString.Exclude
    private String password;

    @NotNull
    @ToString.Exclude
    private String repeatedPassword;

    @Size(min = 2, max = 3, message = "Language can be: EN, PL")
    private String language;

    @NotNull
    @Size(min = 9, max = 9, message = "Phone number can contains only 9 numbers")
    private String phoneNumber;
}
