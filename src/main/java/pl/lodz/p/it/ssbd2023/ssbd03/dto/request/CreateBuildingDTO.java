package pl.lodz.p.it.ssbd2023.ssbd03.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBuildingDTO implements Serializable, Signable {
    @DecimalMin(value = "0")
    private String totalArea;

    @DecimalMin(value = "0")
    private String communalAreaAggregate;

    @Size(max = 32, message = "Max length for street is 32")
    private String street;

    @Pattern(regexp = "^\\d+[a-zA-Z]?$", message = "Invalid number format")
    private String buildingNumber;

    @Size(max = 32, message = "Max length for city is 32")
    private String city;

    @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "Invalid postal code format")
    private String postalCode;

    @Override
    public String messageToSign() {
        return getTotalArea().toString()
                .concat(getCommunalAreaAggregate().toString())
                .concat(getStreet())
                .concat(getBuildingNumber().toString())
                .concat(getCity())
                .concat(getPostalCode());
    }
}
