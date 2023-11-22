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
public class PlaceDTO extends AbstractDTO implements Signable {
    private String createdBy;
    private Short placeNumber;
    private BigDecimal area;
    private Boolean hotWaterConnection;
    private Boolean centralHeatingConnection;
    private BigDecimal predictedHotWaterConsumption;
    private String firstName;
    private String surname;

    public PlaceDTO(Long id, Long version, String createdBy, Short placeNumber, BigDecimal area, Boolean hotWaterConnection, Boolean centralHeatingConnection, BigDecimal predictedHotWaterConsumption, String firstName, String surname) {
        super(id, version);
        this.createdBy = createdBy;
        this.placeNumber = placeNumber;
        this.area = area;
        this.hotWaterConnection = hotWaterConnection;
        this.centralHeatingConnection = centralHeatingConnection;
        this.predictedHotWaterConsumption = predictedHotWaterConsumption;
        this.firstName = firstName;
        this.surname = surname;
    }

    @Override
    public String messageToSign() {
        return getVersion().toString()
                .concat(getPlaceNumber().toString())
                .concat(getArea().toString())
                .concat(getHotWaterConnection().toString())
                .concat(getCentralHeatingConnection().toString())
                .concat(getPredictedHotWaterConsumption().toString())
                .concat(firstName)
                .concat(surname);
    }
}