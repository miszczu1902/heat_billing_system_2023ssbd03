package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.AbstractEntity;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.accounts.Manager;
import pl.lodz.p.it.ssbd2023.ssbd03.util.etag.Signable;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "hot_water_entry",
        indexes = {
                @Index(name = "hot_water_entry_place_id", columnList = "place_id"),
                @Index(name = "hot_water_entry_manager_id", columnList = "manager_id")
        })
@NamedQueries({
        @NamedQuery(name = "HotWaterEntry.checkIfHotWaterEntryCouldBeInserted",
                query = "SELECT e FROM HotWaterEntry e WHERE e.place.id = :placeId AND YEAR(e.date) = :year AND MONTH(e.date) = :month AND e.place.hotWaterConnection = true"),
        @NamedQuery(name = "HotWaterEntry.checkIfHotWaterEntryCouldBeOverwritten",
                query = "SELECT e FROM HotWaterEntry e WHERE e.place.id = :placeId AND YEAR(e.date) = :year AND MONTH(e.date) = :month AND e.manager IS NULL AND e.place.hotWaterConnection = true"),
        @NamedQuery(name = "HotWaterEntry.getListOfHotWaterEntriesForPlace", query = "SELECT k FROM HotWaterEntry k WHERE k.place.id = :id  ORDER BY k.date DESC")
})
public class HotWaterEntry extends AbstractEntity implements Serializable, Signable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_", nullable = false)
    @Setter
    private LocalDate date;

    @Setter
    @DecimalMin(value = "0")
    @Column(name = "entry_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal entryValue;

    @ManyToOne
    @JoinColumn(name = "place_id", updatable = false, referencedColumnName = "id")
    private Place place;

    @Setter
    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    public HotWaterEntry(LocalDate date, BigDecimal entryValue, Place place) {
        this.date = date;
        this.entryValue = entryValue;
        this.place = place;
    }

    @Override
    public String messageToSign() {
        String message = getVersion().toString()
                .concat(getDate().toString())
                .concat(getEntryValue().toString());
        if (getManager() != null) {
            message = message.concat(getManager().getAccount().getUsername());
        }
        return message;
    }
}
