package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "heating_place_and_communal_area_advance")
public final class HeatingPlaceAndCommunalAreaAdvance extends Advance{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private BigDecimal heatingPlaceAdvanceValue;

    @Column(nullable = false)
    private BigDecimal heatingCommunalAreaAdvanceValue;

    @Column(nullable = false)
    private Float advanceChangeFactor;

    public HeatingPlaceAndCommunalAreaAdvance(Long id, LocalDate date, Place place, Long id1, BigDecimal heatingPlaceAdvanceValue, BigDecimal heatingCommunalAreaAdvanceValue, Float advanceChangeFactor) {
        super(id, date, place);
        this.id = id1;
        this.heatingPlaceAdvanceValue = heatingPlaceAdvanceValue;
        this.heatingCommunalAreaAdvanceValue = heatingCommunalAreaAdvanceValue;
        this.advanceChangeFactor = advanceChangeFactor;
    }
}

