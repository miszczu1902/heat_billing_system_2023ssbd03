package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class YearReportDTO {
    private Short year, placeNumber;
    private BigDecimal totalHotWaterAdvance, totalHeatingPlaceAdvance, totalHeatingCommunalAreaAdvance,
            totalHotWaterCost, totalHeatingPlaceCost, totalHeatingCommunalAreaCost,
            hotWaterBalance, heatingPlaceBalance, communalAreaBalance;
    private String street, buildingNumber, city, postalCode;

    public YearReportDTO(Short year, Short placeNumber, BigDecimal totalHotWaterAdvance, BigDecimal totalHeatingPlaceAdvance,
                         BigDecimal totalHeatingCommunalAreaAdvance, BigDecimal totalHotWaterCost, BigDecimal totalHeatingPlaceCost,
                         BigDecimal totalHeatingCommunalAreaCost, String street, String buildingNumber, String city, String postalCode) {
        this.year = year;
        this.placeNumber = placeNumber;
        this.totalHotWaterAdvance = totalHotWaterAdvance;
        this.totalHeatingPlaceAdvance = totalHeatingPlaceAdvance;
        this.totalHeatingCommunalAreaAdvance = totalHeatingCommunalAreaAdvance;
        this.totalHotWaterCost = totalHotWaterCost;
        this.totalHeatingPlaceCost = totalHeatingPlaceCost;
        this.totalHeatingCommunalAreaCost = totalHeatingCommunalAreaCost;
        this.hotWaterBalance = totalHotWaterAdvance.subtract(totalHotWaterCost);
        this.heatingPlaceBalance = totalHeatingPlaceAdvance.subtract(totalHeatingPlaceCost);
        this.communalAreaBalance = totalHeatingCommunalAreaAdvance.subtract(totalHeatingCommunalAreaCost);
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.city = city;
        this.postalCode = postalCode;
    }
}
