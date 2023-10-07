package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AnnualBalanceToListDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.YearReportDTO;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.Address;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.AnnualBalance;

public class BalanceMapper {
    public static AnnualBalanceToListDTO balancesToAnnualBalancesForListDTO(AnnualBalance annualBalance) {
        return new AnnualBalanceToListDTO(
                annualBalance.getId(),
                annualBalance.getVersion(),
                annualBalance.getYear(),
                annualBalance.getPlace().getPlaceNumber(),
                annualBalance.getPlace().getOwner().getAccount().getPersonalData().getFirstName(),
                annualBalance.getPlace().getOwner().getAccount().getPersonalData().getSurname(),
                annualBalance.getPlace().getBuilding().getAddress().getStreet(),
                annualBalance.getPlace().getBuilding().getAddress().getBuildingNumber(),
                annualBalance.getPlace().getBuilding().getAddress().getCity(),
                annualBalance.getPlace().getBuilding().getAddress().getPostalCode(),
                annualBalance.getPlace().getId()
        );
    }

    public static YearReportDTO createYearReportDTOFromAnnualBalance(AnnualBalance annualBalance) {
        Address address = annualBalance.getPlace().getBuilding().getAddress();
        return new YearReportDTO(
                annualBalance.getYear(),
                annualBalance.getPlace().getPlaceNumber(),
                annualBalance.getTotalHotWaterAdvance(),
                annualBalance.getTotalHeatingPlaceAdvance(),
                annualBalance.getTotalHeatingCommunalAreaAdvance(),
                annualBalance.getTotalHotWaterCost(),
                annualBalance.getTotalHeatingPlaceCost(),
                annualBalance.getTotalHeatingCommunalAreaCost(),
                address.getStreet(),
                address.getBuildingNumber(),
                address.getCity(),
                address.getPostalCode()
        );
    }

}
