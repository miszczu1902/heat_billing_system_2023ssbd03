package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnualBalanceToListDTO extends AbstractDTO {
    private Short year;
    private Short placeNumber;
    private String firstName;
    private String surname;
    private String street;
    private String streetNumber;

    public AnnualBalanceToListDTO(Long id, Long version, Short year, Short placeNumber,String firstName, String surname, String street, String streetNumber ) {
        super(id, version);
        this.year = year;
        this.placeNumber=placeNumber;
        this.firstName=firstName;
        this.surname=surname;
        this.street=street;
        this.streetNumber=streetNumber;
    }
}
