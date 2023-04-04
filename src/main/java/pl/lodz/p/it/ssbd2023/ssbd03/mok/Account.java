package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public abstract class Account {
    @Setter(AccessLevel.NONE)
    private int id;
    private String email;
    private String password;
    private Boolean isEnable;
    private LocalDateTime registerDate;
    private String language;
    private LoginData loginData;
    private PersonalData personalData;



}
