package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class HotWaterAdvance extends Advance {

    BigDecimal hotWaterAdvanceValue;


    public HotWaterAdvance(Integer id, LocalDate date, BigDecimal hotWaterAdvanceValue) {
        super(id, date);
        this.hotWaterAdvanceValue = hotWaterAdvanceValue;
    }
}
