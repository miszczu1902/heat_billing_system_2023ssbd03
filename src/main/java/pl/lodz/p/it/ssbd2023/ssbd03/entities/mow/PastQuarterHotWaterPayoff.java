package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AbstractEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "past_quarter_hot_water_pay_off")
public class PastQuarterHotWaterPayoff extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Min(value = 0)
    @Column(name = "average_consumption", nullable = false)
    private BigDecimal averageConsumption;

    @Setter
    @Min(value = 0)
    @Column(name = "days_number_in_quarter", nullable = false)
    private Integer daysNumberInQuarter;
}