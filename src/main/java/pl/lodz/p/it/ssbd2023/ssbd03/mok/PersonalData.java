package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class PersonalData {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private String surname;
}
