package pl.lodz.p.it.ssbd2023.ssbd03.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractDTO {
    private Long id;
    private Long version;
}
