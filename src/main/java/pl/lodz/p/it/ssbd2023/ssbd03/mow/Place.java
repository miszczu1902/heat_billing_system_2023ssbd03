package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private BigDecimal area;

    @Setter
    @Column(nullable = false)
    private Boolean hotWaterConnection;

    @Setter
    @Column(nullable = false)
    private Boolean centralHeatingConnection;

    @Setter
    @Column(nullable = false)
    private BigDecimal predictedHotWaterConsumption;

    @Setter
    @OneToMany(mappedBy = "advances")
    private List<Advance> advances;
}
