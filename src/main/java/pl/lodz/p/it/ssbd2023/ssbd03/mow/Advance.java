package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public sealed abstract class Advance permits HeatingPlaceAndCommunalAreaAdvance, HotWaterAdvance {

    protected Integer id;
    protected LocalDate date;

}
