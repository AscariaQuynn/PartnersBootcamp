package cz.devforce.partnersbootcamp.dto.service;

import java.time.ZonedDateTime;
import java.util.Objects;

public record HelloDo(
    String response,
    ZonedDateTime timestamp
) {
    public HelloDo {
        Objects.requireNonNull(response);
        Objects.requireNonNull(timestamp);
    }
}
