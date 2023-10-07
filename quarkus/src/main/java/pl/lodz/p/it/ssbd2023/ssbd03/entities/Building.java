package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "building",
        indexes = {
                @Index(name = "building_address_id", columnList = "address_id"),
                @Index(name = "building_heat_distribution_centre_id", columnList = "heat_distribution_centre_id")
        })
@NamedNativeQueries({
        @NamedNativeQuery(name = "Building.findAll", query = "SELECT k FROM Building k"),
        @NamedNativeQuery(name = "Building.findById", query = "SELECT k FROM Building k WHERE k.id = :id")
})
public class Building extends AbstractEntity implements Serializable, Signable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DecimalMin(value = "0")
    @Column(name = "total_area", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalArea;

    @Setter
    @DecimalMin(value = "0")
    @Column(name = "communal_area_aggregate", nullable = false, precision = 10, scale = 2)
    private BigDecimal communalAreaAggregate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", updatable = false, referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "building")
    private List<Place> places = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "heat_distribution_centre_id", updatable = false, referencedColumnName = "id")
    private HeatDistributionCentre heatDistributionCentre;

    public Building(BigDecimal totalArea, BigDecimal communalAreaAggregate, Address address, List<Place> places, HeatDistributionCentre heatDistributionCentre) {
        this.totalArea = totalArea;
        this.communalAreaAggregate = communalAreaAggregate;
        this.address = address;
        this.places = places;
        this.heatDistributionCentre = heatDistributionCentre;
    }

    @Override
    public String messageToSign() {
        return getTotalArea().toString()
                .concat(getCommunalAreaAggregate().toString())
                .concat(getAddress().getStreet())
                .concat(getAddress().getBuildingNumber())
                .concat(getAddress().getCity())
                .concat(getAddress().getPostalCode());
    }
}
