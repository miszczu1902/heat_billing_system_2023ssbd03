package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class HeatingPlaceAndCommunalAreaAdvance extends Advance{

    private BigDecimal heatingPlaceAdvanceValue;
    private BigDecimal heatingCommunalAreaAdvanceValue;
    private Float advanceChangeFactor;

    public HeatingPlaceAndCommunalAreaAdvance(Integer id, LocalDate date, BigDecimal heatingPlaceAdvanceValue,
                                              BigDecimal heatingCommunalAreaAdvanceValue, Float advanceChangeFactor) {
        super(id, date);
        this.heatingPlaceAdvanceValue = heatingPlaceAdvanceValue;
        this.heatingCommunalAreaAdvanceValue = heatingCommunalAreaAdvanceValue;
        this.advanceChangeFactor = advanceChangeFactor;
    }
}

