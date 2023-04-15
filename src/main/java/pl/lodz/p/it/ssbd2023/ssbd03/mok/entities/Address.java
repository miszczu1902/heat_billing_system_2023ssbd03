package pl.lodz.p.it.ssbd2023.ssbd03.mok.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.entities.AbstractEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Table(name = "address")
public class Address extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private int place_number;

    @Column(nullable = false)
    private String city;

    @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "Invalid postal code format")
    @Column(nullable = false)
    private String postal_code;
}