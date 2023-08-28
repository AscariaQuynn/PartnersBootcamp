package cz.devforce.partnersbootcamp.service;

import cz.devforce.partnersbootcamp.configuration.RabbitMqProperties;
import cz.devforce.partnersbootcamp.dto.mq.PersonMq;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMqProducerService {

    private final RabbitMqProperties rabbitMqProperties;

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(PersonMq personMq) {
        rabbitTemplate.convertAndSend(rabbitMqProperties.getExchange(), rabbitMqProperties.getRoutingKey(), personMq);
    }
}
