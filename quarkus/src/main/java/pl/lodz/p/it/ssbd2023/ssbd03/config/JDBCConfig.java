package pl.lodz.p.it.ssbd2023.ssbd03.config;

import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2023.ssbd03.util.Boundary;
import jakarta.persistence.EntityManager;

@Boundary
public class JDBCConfig {
    @PersistenceContext(unitName = "ssbd03adminPU")
    EntityManager em;
}
