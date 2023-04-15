package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AbstractEntity;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode
@Entity
@Table(name = "advance")
public sealed abstract class Advance extends AbstractEntity permits HeatingPlaceAndCommunalAreaAdvance, HotWaterAdvance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advance_id")
    protected Long id;

    @Column(nullable = false, name = "date_")
    protected LocalDate date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    protected Place place;
}
