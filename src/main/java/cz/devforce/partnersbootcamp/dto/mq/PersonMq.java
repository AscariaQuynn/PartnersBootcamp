package cz.devforce.partnersbootcamp.dto.mq;

import java.util.Objects;

public record PersonMq(
    String name,
    String surname
) {
    public PersonMq {
        Objects.requireNonNull(name);
        Objects.requireNonNull(surname);
    }
}