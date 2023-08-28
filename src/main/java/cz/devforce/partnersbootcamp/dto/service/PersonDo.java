package cz.devforce.partnersbootcamp.dto.service;

import java.util.Objects;

public record PersonDo(
    String name,
    String surname
) {
    public PersonDo {
        Objects.requireNonNull(name);
        Objects.requireNonNull(surname);
    }
}
