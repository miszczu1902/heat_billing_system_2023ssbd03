package pl.lodz.p.it.ssbd2023.ssbd03.util.mappers;

import pl.lodz.p.it.ssbd2023.ssbd03.dto.response.AnnualBalanceToListDTO;
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
                annualBalance.getPlace().getBuilding().getAddress().getBuildingNumber()
        );}
    }
