package pl.lodz.p.it.ssbd2023.ssbd03.mow.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.entities.AbstractEntity;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "past_quarter_hot_water_pay_off")
public class PastQuarterHotWaterPayoff extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "past_quarter_hot_water_pay_off_id")
    private Long id;

    @Setter
    @Min(value = 0)
    @Column(nullable = false)
    private BigDecimal averageConsumption;

    @Setter
    @Min(value = 0)
    @Column(nullable = false)
    private Integer daysNumberInQuarter;
}