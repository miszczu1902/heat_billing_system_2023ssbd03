package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public abstract class Account {
    @Setter(AccessLevel.NONE)
    protected Long id;
    protected String email;
    protected String password;
    protected Boolean isEnable;
    protected LocalDateTime registerDate;
    protected String language;
    protected LoginData loginData;
    protected PersonalData personalData;
}
