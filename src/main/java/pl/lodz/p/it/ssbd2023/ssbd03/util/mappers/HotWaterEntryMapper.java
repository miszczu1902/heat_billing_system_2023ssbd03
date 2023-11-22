package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.HotWaterEntryDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.HotWaterEntry;

import java.util.Optional;

public class HotWaterEntryMapper {
    public static HotWaterEntryDTO createHotWaterEntryToHotWaterEntryDTO(HotWaterEntry hotWaterEntry) {
        return Optional.ofNullable(hotWaterEntry.getManager())
                .map(value -> new HotWaterEntryDTO(
                        hotWaterEntry.getVersion(),
                        hotWaterEntry.getId(),
                        hotWaterEntry.getDate(),
                        hotWaterEntry.getEntryValue(),
                        hotWaterEntry.getPlace().getId(),
                        value.getAccount().getUsername())).orElseGet(() -> new HotWaterEntryDTO(
                        hotWaterEntry.getVersion(),
                        hotWaterEntry.getId(),
                        hotWaterEntry.getDate(),
                        hotWaterEntry.getEntryValue(),
                        hotWaterEntry.getPlace().getId()));
    }
}
