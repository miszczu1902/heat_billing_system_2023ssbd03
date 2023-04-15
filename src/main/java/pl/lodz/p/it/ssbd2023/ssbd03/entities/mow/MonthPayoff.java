package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.Account;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "month_pay_off")
public class MonthPayoff extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "month_pay_off_id")
    private Long id;

    @Column(nullable = false)
    private LocalDate payoffDate;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal waterHeatingUnitCost;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal centralHeatingUnitCost;

    @DecimalMin(value = "0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hotWaterConsumption;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    private Place place;
}
