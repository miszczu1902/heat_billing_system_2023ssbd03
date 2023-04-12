package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.Owner;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "place")
public class Place extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Setter
    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal area;

    @Setter
    @Column(nullable = false)
    private Boolean hotWaterConnection;

    @Setter
    @Column(nullable = false)
    private Boolean centralHeatingConnection;

    @Setter
    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal predictedHotWaterConsumption;

    @Setter
    @OneToMany(mappedBy = "advances")
    private List<Advance> advances;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "building_id")
    private Building building;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "past_quarter_hot_water_payoff_id")
    private PastQuarterHotWaterPayoff pastQuarterHotWaterPayoff;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private Owner owner;
}
