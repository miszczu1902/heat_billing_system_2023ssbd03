package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "month_pay_off")
public class MonthPayoff extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "payoff_date", nullable = false)
    private LocalDate payoffDate;

    @DecimalMin(value = "0")
    @Column(name = "water_heating_unit_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal waterHeatingUnitCost;

    @DecimalMin(value = "0")
    @Column(name = "central_heating_unit_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal centralHeatingUnitCost;

    @DecimalMin(value = "0")
    @Column(name = "hot_water_consumption", nullable = false, precision = 10, scale = 2)
    private BigDecimal hotWaterConsumption;

    @ManyToOne()
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;
}
