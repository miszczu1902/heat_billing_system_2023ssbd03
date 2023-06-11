package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AdvanceForMonthDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatingPlaceAndCommunalAreaAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterAdvance;

import java.util.List;

public class AdvanceMapper {
    public static List<AdvanceForMonthDTO> createListOfAdvanceForMonthDTOFromListOfAdvances(List<HotWaterAdvance> hotWaterAdvances,
                                                                                            List<HeatingPlaceAndCommunalAreaAdvance> heatingAdvances) {
        List<AdvanceForMonthDTO> result = heatingAdvances.stream()
                .map(advance ->
                        new AdvanceForMonthDTO(
                                advance.getHeatingPlaceAdvanceValue(),
                                advance.getHeatingCommunalAreaAdvanceValue(),
                                advance.getDate().getMonthValue(),
                                advance.getDate().getYear()))
                .toList();

        result.forEach(advanceToDisplay -> {
            hotWaterAdvances.forEach(hotWaterAdvance -> {
                if (advanceToDisplay.getYear() == hotWaterAdvance.getDate().getYear()
                        && advanceToDisplay.getMonth() == hotWaterAdvance.getDate().getMonthValue())
                    advanceToDisplay.setHotWaterAdvanceValue(hotWaterAdvance.getHotWaterAdvanceValue());
            });
        });
        return result;
    }
}
