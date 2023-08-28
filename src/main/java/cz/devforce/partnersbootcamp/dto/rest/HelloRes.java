package cz.devforce.partnersbootcamp.dto.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Objects;

public record HelloRes(

        @JsonProperty(required = true) String response,
        @JsonProperty(required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        ZonedDateTime timestamp
) {
    public HelloRes {
        Objects.requireNonNull(response);
        Objects.requireNonNull(timestamp);
    }
}