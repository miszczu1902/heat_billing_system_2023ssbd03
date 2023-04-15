package pl.lodz.p.it.ssbd2023.ssbd03.mok.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.entities.AccessLevelMapping;
import pl.lodz.p.it.ssbd2023.ssbd03.mok.entities.Address;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("OWNER")
@Table(name = "owner")
public class Owner extends AccessLevelMapping {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    public Owner(Address address) {
        this.address = address;
    }
}