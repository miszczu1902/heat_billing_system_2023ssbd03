package pl.lodz.p.it.ssbd2023.ssbd03.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractDTO {
    protected Long id;
    protected Long version;
}
