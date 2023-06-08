package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.AbstractDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Building;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlacesListDTO extends AbstractDTO implements Signable {
    private Short placeNumber;
    private BigDecimal area;
    private Boolean hotWaterConnection;
    private Boolean centralHeatingConnection;
    private BigDecimal predictedHotWaterConsumption;
    private String street;
    private String buildingNumber;
    private String city;
    private String postalCode;

    public PlacesListDTO(Long id, Long version, Short placeNumber, BigDecimal area, Boolean hotWaterConnection, Boolean centralHeatingConnection, BigDecimal predictedHotWaterConsumption, String street, String buildingNumber, String city, String postalCode) {
        super(id, version);
        this.placeNumber = placeNumber;
        this.area = area;
        this.hotWaterConnection = hotWaterConnection;
        this.centralHeatingConnection = centralHeatingConnection;
        this.predictedHotWaterConsumption = predictedHotWaterConsumption;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.city = city;
        this.postalCode = postalCode;
    }

    @Override
    public String messageToSign() {
        return getVersion().toString()
                .concat(getPlaceNumber().toString())
                .concat(getArea().toString())
                .concat(getHotWaterConnection().toString())
                .concat(getCentralHeatingConnection().toString())
                .concat(getPredictedHotWaterConsumption().toString())
                .concat(getStreet())
                .concat(getBuildingNumber().toString())
                .concat(getCity())
                .concat(getPostalCode());
    }
}