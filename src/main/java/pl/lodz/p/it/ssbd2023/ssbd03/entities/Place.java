package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "place",
        indexes = {
                @Index(name = "place_building_id", columnList = "building_id"),
                @Index(name = "place_owner_id", columnList = "owner_id")
        })
public class Place extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
    @JoinColumn(name = "owner_id", updatable = false, referencedColumnName = "id")
    private Owner owner;

    @OneToMany(mappedBy = "place")
    private List<HotWaterEntry> hotWaterEntries = new ArrayList<>();

    @OneToMany(mappedBy = "place")
    private List<MonthPayoff> monthPayoffs = new ArrayList<>();

    @OneToMany(mappedBy = "place")
    private List<AnnualBalance> annualBalances = new ArrayList<>();
}
