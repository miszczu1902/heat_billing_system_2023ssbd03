package pl.lodz.p.it.ssbd2023.ssbd03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceForMonthInYearDTO {
    private BigDecimal heatingPlaceAdvanceValue;
    private BigDecimal heatingCommunalAreaAdvanceValue;
    private BigDecimal hotWaterAdvanceValue;
    private Integer month;
    private Integer year;

    public AdvanceForMonthInYearDTO(BigDecimal heatingPlaceAdvanceValue, BigDecimal heatingCommunalAreaAdvanceValue, Integer month, Integer year) {
        this.heatingPlaceAdvanceValue = heatingPlaceAdvanceValue;
        this.heatingCommunalAreaAdvanceValue = heatingCommunalAreaAdvanceValue;
        this.month = month;
        this.year = year;
    }

    public AdvanceForMonthInYearDTO(BigDecimal hotWaterAdvanceValue, Integer month, Integer year) {
        this.hotWaterAdvanceValue = hotWaterAdvanceValue;
        this.month = month;
        this.year = year;
    }
}
