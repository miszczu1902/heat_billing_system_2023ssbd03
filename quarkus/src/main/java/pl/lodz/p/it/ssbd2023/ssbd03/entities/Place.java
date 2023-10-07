package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Owner;
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
@Table(name = "place",
        indexes = {
                @Index(name = "place_building_id", columnList = "building_id"),
                @Index(name = "place_owner_id", columnList = "owner_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "place_number_building_id_unique_constraint", columnNames = {"place_number", "building_id"})
        })
@NamedNativeQueries({
        @NamedNativeQuery(name = "Place.findPlacesByBuildingId", query = "SELECT k FROM Place k WHERE k.building.id = :id"),
        @NamedNativeQuery(name = "Place.findById", query = "SELECT k FROM Place k WHERE k.id = :id"),
        @NamedNativeQuery(name = "Place.findPlacesByOwner", query = "SELECT k FROM Place k WHERE k.owner.id = :id"),
        @NamedNativeQuery(name = "Place.findPlaceByUsernameAndCheckIfHeIsOwnerOfPlace",
                query = "SELECT k FROM Place k WHERE k.id = :placeId AND k.owner.account.username = :username"),
        @NamedNativeQuery(name = "Place.findAllPlaces", query = "SELECT k FROM Place k"),
        @NamedNativeQuery(name = "Place.findAllPlacesByBuildingIdAndNewerThanDate",
                query = "SELECT k FROM Place k WHERE k.building.id = :buildingId AND k.creationDateTime >= :date"),
        @NamedNativeQuery(name = "Place.findAllPlacesAddedBeforeDate",
                query = "SELECT k FROM Place k WHERE k.creationDateTime < :date"),
})
public class Place extends AbstractEntity implements Serializable, Signable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "place_number", nullable = false)
    private Short placeNumber;

    @Setter
    @DecimalMin(value = "0")
    @Column(name = "area", nullable = false, precision = 10, scale = 2)
    private BigDecimal area;

    @Setter
    @Column(name = "hot_water_connection", nullable = false)
    private Boolean hotWaterConnection;

    @Setter
    @Column(name = "central_heating_connection", nullable = false)
    private Boolean centralHeatingConnection;

    @Setter
    @DecimalMin(value = "0")
    @Column(name = "predicted_hot_water_consumption", nullable = false, precision = 10, scale = 2)
    private BigDecimal predictedHotWaterConsumption;

    @Setter
    @OneToMany(mappedBy = "place")
    private List<Advance> advances = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "building_id", updatable = false, referencedColumnName = "id")
    private Building building;

    @ManyToOne
    @Setter
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;

    @OneToMany(mappedBy = "place")
    private List<HotWaterEntry> hotWaterEntries = new ArrayList<>();

    @OneToMany(mappedBy = "place")
    private List<MonthPayoff> monthPayoffs = new ArrayList<>();

    @OneToMany(mappedBy = "place")
    private List<AnnualBalance> annualBalances = new ArrayList<>();

    public Place(Short placeNumber, BigDecimal area, Boolean hotWaterConnection,
                 Boolean centralHeatingConnection, BigDecimal predictedHotWaterConsumption, Building building, Owner owner) {
        this.placeNumber = placeNumber;
        this.area = area;
        this.hotWaterConnection = hotWaterConnection;
        this.centralHeatingConnection = centralHeatingConnection;
        this.predictedHotWaterConsumption = predictedHotWaterConsumption;
        this.building = building;
        this.owner = owner;
    }

    @Override
    public String messageToSign() {
        return getVersion().toString()
                .concat(getArea().toString())
                .concat(getPredictedHotWaterConsumption().toString())
                .concat(owner.getAccount().getUsername());
    }
}
