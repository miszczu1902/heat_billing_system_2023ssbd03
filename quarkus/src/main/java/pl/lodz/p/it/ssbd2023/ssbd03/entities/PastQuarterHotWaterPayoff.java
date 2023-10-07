package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", updatable = false, referencedColumnName = "id")
    private Place id;

    @Setter
    @Min(value = 0)
    @Column(name = "average_consumption", nullable = false, precision = 10, scale = 2)
    private BigDecimal averageConsumption;

    @Setter
    @Min(value = 90)
    @Max(value = 92)
    @Column(name = "days_number_in_quarter", nullable = false)
    private Integer daysNumberInQuarter;
}