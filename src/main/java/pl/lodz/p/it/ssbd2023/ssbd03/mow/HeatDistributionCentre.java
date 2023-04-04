package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class HeatDistributionCentre {

    @NonNull
    private Integer id;

    private List<Building> buildings;

    public void addBuilding(Building building) {

    }

}
