package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AbstractEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "building")
public class Building extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DecimalMin(value = "0")
    @Column(name = "total_area", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalArea;

    @DecimalMin(value = "0")
    @Column(name = "communal_area_aggregate", nullable = false, precision = 10, scale = 2)
    private BigDecimal communalAreaAggregate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "building")
    private List<Place> places;

    @ManyToOne()
    @JoinColumn(name = "heat_distribution_centre_id", referencedColumnName = "id")
    private HeatDistributionCentre heatDistributionCentre;

}
