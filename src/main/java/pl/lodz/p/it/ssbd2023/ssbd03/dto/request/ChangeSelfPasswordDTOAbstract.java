package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractVersionDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeSelfPasswordDTOAbstract extends AbstractVersionDTO {
    @NotNull
    private String oldPassword;
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Restrictions for password is: at least 8 characters length, at least one upper and lower case, number and special digit")
    private String newPassword;
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Restrictions for password is: at least 8 characters length, at least one upper and lower case, number and special digit")
    private String repeatedNewPassword;
}
