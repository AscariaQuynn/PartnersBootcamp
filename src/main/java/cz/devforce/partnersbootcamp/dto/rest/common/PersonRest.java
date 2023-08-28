package cz.devforce.partnersbootcamp.dto.rest.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record PersonRest(
    @JsonProperty(required = true) String name,
    @JsonProperty(required = true) String surname
) {
    public PersonRest {
        Objects.requireNonNull(name);
        Objects.requireNonNull(surname);
    }
}
