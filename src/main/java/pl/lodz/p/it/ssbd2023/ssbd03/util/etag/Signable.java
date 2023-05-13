package pl.lodz.p.it.ssbd2023.ssbd03.util.etag;

import jakarta.json.bind.annotation.JsonbTransient;

public interface Signable {

    @JsonbTransient
    String messageToSign();
}
