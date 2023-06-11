package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AdvanceForMonthDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HeatingPlaceAndCommunalAreaAdvance;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterAdvance;

import java.util.List;
import java.util.stream.Stream;

public class AdvanceMapper {
    public static List<AdvanceForMonthDTO> createListOfAdvanceForMonthDTOFromListOfAdvances(List<HotWaterAdvance> hotWaterAdvances,
                                                                                            List<HeatingPlaceAndCommunalAreaAdvance> heatingAdvances) {
        List<AdvanceForMonthDTO> resultToDisplay = hotWaterAdvances.stream()
                .map(advance ->
                        new AdvanceForMonthDTO(
                                advance.getHotWaterAdvanceValue(),
                                advance.getDate().getMonthValue(),
                                advance.getDate().getYear()))
                .toList();
        resultToDisplay.forEach(advance -> heatingAdvances.forEach(heatingAdvance -> {
            if (heatingAdvance.getDate().getYear() == advance.getYear()
                    && heatingAdvance.getDate().getMonthValue() == advance.getMonth()) {
                advance.setHeatingPlaceAdvanceValue(heatingAdvance.getHeatingPlaceAdvanceValue());
                advance.setHeatingCommunalAreaAdvanceValue(heatingAdvance.getHeatingCommunalAreaAdvanceValue());
            }
        }));
        resultToDisplay.forEach(advance -> heatingAdvances.removeIf(heatingAdvance -> heatingAdvance.getDate().getYear() == advance.getYear()
                && heatingAdvance.getDate().getMonthValue() == advance.getMonth()));

        if (!heatingAdvances.isEmpty()) {
            resultToDisplay = Stream.concat(resultToDisplay.stream(),
                    heatingAdvances.stream()
                            .map(advance ->
                                    new AdvanceForMonthDTO(
                                            advance.getHeatingPlaceAdvanceValue(),
                                            advance.getHeatingCommunalAreaAdvanceValue(),
                                            advance.getDate().getMonthValue(),
                                            advance.getDate().getYear()))).toList();
        }

        return resultToDisplay;
    }
}
