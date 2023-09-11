package cz.devforce.partnersbootcamp.rest;

import cz.devforce.partnersbootcamp.common.DateTimeUtils;
import cz.devforce.partnersbootcamp.dto.rest.FileUploadStatusRes;
import cz.devforce.partnersbootcamp.dto.rest.HelloRes;
import cz.devforce.partnersbootcamp.dto.rest.PersonListRes;
import cz.devforce.partnersbootcamp.dto.rest.common.PersonRest;
import cz.devforce.partnersbootcamp.dto.service.PersonDo;
import cz.devforce.partnersbootcamp.service.FileUploadService;
import cz.devforce.partnersbootcamp.service.HelloWorldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/rest/bootcamp")
@RequiredArgsConstructor
public class RestBootcamp {

    private final HelloWorldService helloWorldService;

    private final FileUploadService fileUploadService;

    @GetMapping(path = "/hello")
    public HelloRes helloWorld() {
        return new HelloRes(
            "Hello World!",
            DateTimeUtils.now()
        );
    }

    @PostMapping(path = "/hello/person")
    public HelloRes helloPerson(@RequestBody PersonRest personReq) {
        // skipped mapping, like mapstruct
        var helloDo = helloWorldService.helloWorld(new PersonDo(
            personReq.name(),
            personReq.surname()
        ));
        // skipped mapping, like mapstruct
        return new HelloRes(
            helloDo.response(),
            helloDo.timestamp()
        );
    }

    @GetMapping(path = "/hello/person")
    public PersonListRes helloPerson() {
        // Keep
        var personListDo = helloWorldService.helloWorld();
        // It
        var personListRes = personListDo.personList().stream().map(personDo -> new PersonRest(
            personDo.name(),
            personDo.surname()
        )).collect(Collectors.toCollection(ArrayList::new));
        // Separated :)
        return new PersonListRes(personListRes);
    }

    @PostMapping(path = "/upload-file")
    public FileUploadStatusRes uploadFile(
        @RequestPart("file") MultipartFile file
    ) throws IOException {
        var uploadStatusDo = fileUploadService.uploadFile(file.getBytes());
        return new FileUploadStatusRes(
            uploadStatusDo.id(),
            uploadStatusDo.uploadStatus()
        );
    }

    @GetMapping(path = "/upload-file/{id}/status")
    public FileUploadStatusRes getUploadFileStatus(@PathVariable("id") Long id) {
        var uploadStatusDo = fileUploadService.getUploadFileStatus(id);
        return new FileUploadStatusRes(
            uploadStatusDo.id(),
            uploadStatusDo.uploadStatus()
        );
    }
}
