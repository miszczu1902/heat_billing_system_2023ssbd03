package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.AbstractEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "heat_distribution_centre")
@NamedNativeQueries({
        @NamedNativeQuery(name = "HeatDistributionCentre.getListOfHeatDistributionCentre", query = "SELECT k FROM HeatDistributionCentre k "),
        @NamedNativeQuery(name = "HeatDistributionCentre.findById", query = "SELECT k from HeatDistributionCentre k where k.id = :id")
})
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
