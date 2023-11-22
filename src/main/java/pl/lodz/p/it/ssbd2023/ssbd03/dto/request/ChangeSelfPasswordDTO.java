package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.VersionDTO;

@Data
@NoArgsConstructor
public class ChangeSelfPasswordDTO extends VersionDTO {
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

    public ChangeSelfPasswordDTO(String oldPassword, String newPassword, String repeatedNewPassword, @NotNull Long version) {
        super(version);
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.repeatedNewPassword = repeatedNewPassword;
    }
}
