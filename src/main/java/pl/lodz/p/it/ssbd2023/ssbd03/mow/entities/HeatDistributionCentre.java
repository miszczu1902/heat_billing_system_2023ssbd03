package pl.lodz.p.it.ssbd2023.ssbd03.mow.entities;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.mow.entities.Building;

import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "heat_distribution_centre")
public class HeatDistributionCentre extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heat_distribution_centre_id")
    private Long id;

    @OneToMany(mappedBy = "building")
    private List<Building> buildings;
}
