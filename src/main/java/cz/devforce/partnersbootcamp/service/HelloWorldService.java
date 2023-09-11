package cz.devforce.partnersbootcamp.service;

import cz.devforce.partnersbootcamp.common.DateTimeUtils;
import cz.devforce.partnersbootcamp.configuration.MeaningOfLifeProperties;
import cz.devforce.partnersbootcamp.dto.entity.PersonDao;
import cz.devforce.partnersbootcamp.dto.mq.PersonMq;
import cz.devforce.partnersbootcamp.dto.mq.ProcessedFileMq;
import cz.devforce.partnersbootcamp.dto.service.HelloDo;
import cz.devforce.partnersbootcamp.dto.service.PersonDo;
import cz.devforce.partnersbootcamp.dto.service.PersonListDo;
import cz.devforce.partnersbootcamp.repository.PersonsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelloWorldService {

    private final MeaningOfLifeProperties meaningOfLifeProperties;

    private final PersonsRepository personsRepository;

    private final RabbitMqProducerService rabbitMqProducerService;

    public HelloDo helloWorld(PersonDo personDo) {
        // Every visitor needs to be stored
        var personDao = personsRepository.save(new PersonDao(
            personDo.name(),
            personDo.surname()
        ));
        // Also, send them to MQ for fun
        rabbitMqProducerService.sendMessage(new PersonMq(
            personDo.name(),
            personDo.surname()
        ));
        // Congratulate and inform visitor about stored id
        return new HelloDo(
            personDo.name() + " " + personDo.surname() + ", Hello World! The answer is " + meaningOfLifeProperties.getMeaningOfLife() + "."
                + " By the way, your name and surname were stored in DB with id " + personDao.getId() + "."
                + " Also, they were sent to RabbitMQ so don't forget to look into logs for receiving confirmation log.",
            DateTimeUtils.now()
        );
    }

    public PersonListDo helloWorld() {
        // First find everything
        var personDaoList = personsRepository.findAll();
        // Magically transform with stream
        var personListDo = personDaoList.stream().map(personDao -> new PersonDo(
            personDao.getName(),
            personDao.getSurname()
        )).collect(Collectors.toCollection(ArrayList::new));
        // Return correct Domain object
        return new PersonListDo(personListDo);
    }

    @RabbitListener(bindings = {
        @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.queue.default}", durable = "true"),
            exchange = @Exchange(value = "${spring.rabbitmq.exchange}"),
            key = "${spring.rabbitmq.routing-key.default}")
    })
    public void receivedMessage(PersonMq personMq) {
        log.info("Received message from RabbitMq: " + personMq.toString());
        // Store visitor received through RabbitMQ
        personsRepository.save(new PersonDao(
            personMq.name() + "MQ",
            personMq.surname() + "MQ"
        ));
    }

    @RabbitListener(bindings = {
        @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.queue.file-processed}", durable = "true"),
            exchange = @Exchange(value = "${spring.rabbitmq.exchange}"),
            key = "${spring.rabbitmq.routing-key.file-processed}")
    })
    public void receivedProcessedFile(ProcessedFileMq processedFileMq) {
        log.info("Received ProcessedFileMq from RabbitMq: " + processedFileMq.toString());
    }
}
