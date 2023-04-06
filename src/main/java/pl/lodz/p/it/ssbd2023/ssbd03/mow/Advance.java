package pl.lodz.p.it.ssbd2023.ssbd03.mow;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "advance")
public sealed abstract class Advance permits HeatingPlaceAndCommunalAreaAdvance, HotWaterAdvance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, name = "date_")
    protected LocalDate date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    protected Place place;
}
