package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "past_quarter_hot_water_pay_off")
public class PastQuarterHotWaterPayoff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private BigDecimal averageConsumption;

    @Setter
    @Column(nullable = false)
    private Integer daysNumberInQuarter;

}
