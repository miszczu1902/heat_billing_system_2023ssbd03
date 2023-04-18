package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.Owner;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "place")
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
    private List<Advance> advances;

    @ManyToOne()
    @JoinColumn(name = "building_id", referencedColumnName = "id")
    private Building building;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "past_quarter_hot_water_payoff_id", referencedColumnName = "id")
    private PastQuarterHotWaterPayoff pastQuarterHotWaterPayoff;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;
}
