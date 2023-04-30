package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "heat_distribution_centre")
public class HeatDistributionCentre extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "heatDistributionCentre")
    private List<Building> buildings = new ArrayList<>();

    @OneToMany(mappedBy = "heatDistributionCentre")
    private List<HeatDistributionCentrePayoff> heatDistributionCentrePayoffs = new ArrayList<>();

}
