package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Building {

    private Integer id;
    private BigDecimal totalArea;
    private BigDecimal communalAreaAggregate;
    private String address;
    private List<Place> places;

    public void addPlace(Place place) {

    }

}
