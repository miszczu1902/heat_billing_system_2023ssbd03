package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Owner;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "month_pay_off",
        indexes = {
                @Index(name = "month_pay_off_place_id", columnList = "place_id"),
                @Index(name = "month_pay_off_owner_id", columnList = "owner_id")
        })
@NamedNativeQueries({
        @NamedNativeQuery(name = "MonthPayoff.findWaterHeatingUnitCost", query = "SELECT k FROM MonthPayoff k WHERE k.waterHeatingUnitCost != 0 ORDER BY k.payoffDate DESC")
})
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

    @ManyToOne
    @JoinColumn(name = "place_id", updatable = false, referencedColumnName = "id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "owner_id", updatable = false, referencedColumnName = "id")
    private Owner owner;

    public MonthPayoff(LocalDate payoffDate, BigDecimal waterHeatingUnitCost, BigDecimal centralHeatingUnitCost, BigDecimal hotWaterConsumption, Place place, Owner owner) {
        this.payoffDate = payoffDate;
        this.waterHeatingUnitCost = waterHeatingUnitCost;
        this.centralHeatingUnitCost = centralHeatingUnitCost;
        this.hotWaterConsumption = hotWaterConsumption;
        this.place = place;
        this.owner = owner;
    }
}
