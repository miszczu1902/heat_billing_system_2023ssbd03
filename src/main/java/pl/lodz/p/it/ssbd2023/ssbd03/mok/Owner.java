package pl.lodz.p.it.ssbd2023.ssbd03.mok;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "owner")
public class Owner extends Account {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    public Owner(Long id, String email, String password, Boolean isEnable, LocalDateTime registerDate, String language, LoginData loginData, PersonalData personalData, Address address) {
        super(id, email, password, isEnable, registerDate, language, loginData, personalData);
        this.address = address;
    }
}