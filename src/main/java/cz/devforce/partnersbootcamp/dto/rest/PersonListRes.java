package cz.devforce.partnersbootcamp.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.devforce.partnersbootcamp.dto.rest.common.PersonRest;

import java.util.List;
import java.util.Objects;

public record PersonListRes(

        @JsonProperty(required = true) List<PersonRest> personList
) {
    public PersonListRes {
        Objects.requireNonNull(personList);
    }
}
