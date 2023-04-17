package pl.lodz.p.it.ssbd2023.ssbd03.entities.mok;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("OWNER")
@Table(name = "owner")
public class Owner extends AccessLevelMapping implements Serializable {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    public Owner(Address address) {
        this.address = address;
    }
}