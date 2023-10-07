package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode
@Entity
@Table(name = "advance",
        indexes = {
                @Index(name = "advance_place_id", columnList = "place_id")
        })
@NamedQueries({
        @NamedQuery(name = "Advance.findAllHotWaterAdvancesForPlace", query = "SELECT a FROM HotWaterAdvance a WHERE a.place.id = :placeId AND YEAR(a.date) = :year ORDER BY a.date DESC"),
        @NamedQuery(name = "Advance.findAllHeatingPlaceAndCommunalAreaAdvancesForPlace", query = "SELECT a FROM HeatingPlaceAndCommunalAreaAdvance a WHERE a.place.id = :placeId AND YEAR(a.date) = :year ORDER BY a.date DESC")
})
public abstract class Advance extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "date_", nullable = false)
    protected LocalDate date;

    @ManyToOne
    @JoinColumn(name = "place_id", updatable = false, referencedColumnName = "id")
    protected Place place;

    public Advance(LocalDate date, Place place) {
        this.date = date;
        this.place = place;
    }
}
