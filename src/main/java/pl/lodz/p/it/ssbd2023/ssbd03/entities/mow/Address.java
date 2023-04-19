package pl.lodz.p.it.ssbd2023.ssbd03.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.entities.mok.AbstractEntity;

import java.io.Serializable;

@Getter
@Entity
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "building_number", nullable = false)
    private short buildingNumber;

    @Column(name = "city", nullable = false)
    private String city;

    @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "Invalid postal code format")
    @Column(name = "postal_code", nullable = false)
    private String postalCode;
}
