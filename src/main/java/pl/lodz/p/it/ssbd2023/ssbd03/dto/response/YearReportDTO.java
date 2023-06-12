package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearReportDTO {
    private Short year, placeNumber;
    private BigDecimal totalHotWaterAdvance, totalHeatingPlaceAdvance, totalHeatingCommunalAreaAdvance,
            totalHotWaterCost, totalHeatingPlaceCost, totalHeatingCommunalAreaCost;
    private String street, buildingNumber, city, postalCode;
}
