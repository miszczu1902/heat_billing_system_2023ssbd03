package pl.lodz.p.it.ssbd2023.ssbd03.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "address")
public class Address extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street", nullable = false, length = 32)
    private String street;

    @Column(name = "building_number", nullable = false)
    private Short buildingNumber;

    @Column(name = "city", nullable = false, length = 32)
    private String city;

    @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "Invalid postal code format")
    @Column(name = "postal_code", nullable = false, length = 6)
    private String postalCode;
}
