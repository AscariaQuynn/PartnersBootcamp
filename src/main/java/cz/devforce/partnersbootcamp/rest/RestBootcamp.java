package cz.devforce.partnersbootcamp.rest;

import cz.devforce.partnersbootcamp.dto.rest.PersonListRes;
import cz.devforce.partnersbootcamp.dto.rest.HelloRes;
import cz.devforce.partnersbootcamp.dto.rest.common.PersonRest;
import cz.devforce.partnersbootcamp.dto.service.PersonDo;
import cz.devforce.partnersbootcamp.service.BusinessBootcampService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/rest/bootcamp")
@RequiredArgsConstructor
public class RestBootcamp {

    private final BusinessBootcampService businessBootcampService;

    @PostMapping(path = "/hello")
    public HelloRes helloWorld(@RequestBody PersonRest personReq) {
        // skipped mapping, like mapstruct
        var helloDo = businessBootcampService.helloWorld(new PersonDo(
            personReq.name(),
            personReq.surname()
        ));
        // skipped mapping, like mapstruct
        return new HelloRes(
            helloDo.response(),
            helloDo.timestamp()
        );
    }

    @GetMapping(path = "/hello")
    public PersonListRes helloWorld() {
        // Keep
        var personListDo = businessBootcampService.helloWorld();
        // It
        var personListRes = personListDo.personList().stream().map(personDo -> new PersonRest(
            personDo.name(),
            personDo.surname()
        )).collect(Collectors.toCollection(ArrayList::new));
        // Separated :)
        return new PersonListRes(personListRes);
    }
}
