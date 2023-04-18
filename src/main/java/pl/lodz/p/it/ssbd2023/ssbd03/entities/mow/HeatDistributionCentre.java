package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AbstractEntity;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "heat_distribution_centre")
public class HeatDistributionCentre extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "heatDistributionCentre")
    private List<Building> buildings;
}
