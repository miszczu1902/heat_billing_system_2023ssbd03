package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AdvanceForMonthDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Advance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatingPlaceAndCommunalAreaAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterAdvance;

public class AdvanceMapper {
    public static AdvanceForMonthDTO createAdvanceForMonthDTOFromAdvance(Advance advance) {
        AdvanceForMonthDTO advanceForMonthDTO = new AdvanceForMonthDTO(advance.getDate().getMonthValue(), advance.getDate().getYear());
        if (advance instanceof HeatingPlaceAndCommunalAreaAdvance) {
            advanceForMonthDTO.setHeatingPlaceAdvanceValue(((HeatingPlaceAndCommunalAreaAdvance) advance).getHeatingPlaceAdvanceValue());
            advanceForMonthDTO.setHeatingPlaceAdvanceValue(((HeatingPlaceAndCommunalAreaAdvance) advance).getHeatingCommunalAreaAdvanceValue());
        } else if (advance instanceof HotWaterAdvance) {
            advanceForMonthDTO.setHotWaterAdvanceValue(((HotWaterAdvance) advance).getHotWaterAdvanceValue());
        }
        return advanceForMonthDTO;
    }
}
