package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingDTO extends AbstractDTO implements Signable {
    private String createdBy;
    private BigDecimal totalArea;
    private BigDecimal communalAreaAggregate;
    private String street;
    private String buildingNumber;
    private String city;
    private String postalCode;

    public BuildingDTO(Long id, Long version, String createdBy, BigDecimal totalArea, BigDecimal communalAreaAggregate, String street, String buildingNumber, String city, String postalCode) {
        super(id, version);
        this.createdBy = createdBy;
        this.totalArea = totalArea;
        this.communalAreaAggregate = communalAreaAggregate;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.city = city;
        this.postalCode = postalCode;
    }


    @Override
    public String messageToSign() {
        return getVersion().toString()
                .concat(getTotalArea().toString())
                .concat(getCommunalAreaAggregate().toString())
                .concat(getStreet())
                .concat(getBuildingNumber().toString())
                .concat(getCity())
                .concat(getPostalCode());
    }
}
