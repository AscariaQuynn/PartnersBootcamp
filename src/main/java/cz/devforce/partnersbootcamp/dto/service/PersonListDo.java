package cz.devforce.partnersbootcamp.dto.service;

import java.util.List;
import java.util.Objects;

public record PersonListDo(
    List<PersonDo> personList
) {
    public PersonListDo {
        Objects.requireNonNull(personList);
    }
}
