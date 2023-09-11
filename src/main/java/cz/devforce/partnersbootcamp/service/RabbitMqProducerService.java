package cz.devforce.partnersbootcamp.service;

import cz.devforce.partnersbootcamp.configuration.RabbitMqProperties;
import cz.devforce.partnersbootcamp.dto.mq.ProcessedFileMq;
import cz.devforce.partnersbootcamp.dto.mq.ProcessingFileMq;
import cz.devforce.partnersbootcamp.dto.mq.PersonMq;
import cz.devforce.partnersbootcamp.dto.mq.AnalyzingFileMq;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMqProducerService {

    private final RabbitMqProperties rabbitMqProperties;

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(PersonMq personMq) {
        rabbitTemplate.convertAndSend(
            rabbitMqProperties.getExchange(),
            rabbitMqProperties.getDefaultRoutingKey(),
            personMq
        );
    }

    public void sendFileAnalyzing(AnalyzingFileMq analyzingFileMq) {
        rabbitTemplate.convertAndSend(
            rabbitMqProperties.getExchange(),
            rabbitMqProperties.getFileUploadedRoutingKey(),
            analyzingFileMq
        );
    }

    public void sendFileProcessing(ProcessingFileMq processingFileMq) {
        rabbitTemplate.convertAndSend(
            rabbitMqProperties.getExchange(),
            rabbitMqProperties.getFileProcessingRoutingKey(),
            processingFileMq
        );
    }

    public void sendFileProcessed(ProcessedFileMq processedFileMq) {
        rabbitTemplate.convertAndSend(
            rabbitMqProperties.getExchange(),
            rabbitMqProperties.getFileProcessedRoutingKey(),
            processedFileMq
        );
    }
}
