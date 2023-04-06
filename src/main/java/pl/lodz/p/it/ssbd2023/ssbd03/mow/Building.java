package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "building")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalArea;

    @Column(nullable = false)
    private BigDecimal communalAreaAggregate;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "place")
    private List<Place> places;

    public void addPlace(Place place) {

    }

}
