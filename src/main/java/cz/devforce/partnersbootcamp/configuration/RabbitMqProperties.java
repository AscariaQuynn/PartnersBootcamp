package cz.devforce.partnersbootcamp.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class RabbitMqProperties {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.queue.default}")
    private String defaultQueue;

    @Value("${spring.rabbitmq.queue.file-uploaded}")
    private String fileUploadedQueue;

    @Value("${spring.rabbitmq.queue.file-processing}")
    private String fileProcessingQueue;

    @Value("${spring.rabbitmq.queue.file-processed}")
    private String fileProcessedQueue;

    @Value("${spring.rabbitmq.routing-key.default}")
    private String defaultRoutingKey;

    @Value("${spring.rabbitmq.routing-key.file-uploaded}")
    private String fileUploadedRoutingKey;

    @Value("${spring.rabbitmq.routing-key.file-processing}")
    private String fileProcessingRoutingKey;

    @Value("${spring.rabbitmq.routing-key.file-processed}")
    private String fileProcessedRoutingKey;
}
