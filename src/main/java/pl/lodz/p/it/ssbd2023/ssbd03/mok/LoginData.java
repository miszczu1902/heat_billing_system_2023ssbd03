package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class LoginData {
    @Setter(AccessLevel.NONE)
    private int id;
    private LocalDateTime lastValidLoginDate;
    private String lastValidLogicAddress;
    private LocalDateTime lastInvalidLoginDate;
    private String lastInvalidLogicAddress;
    private int invalidLoginCounter;

}
