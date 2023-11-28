package pl.lodz.p.it.ssbd2023.ssbd03.health;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
public class AppStatus {
    @Setter
    @Getter
    private boolean isAvailable = true;
}
