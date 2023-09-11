package cz.devforce.partnersbootcamp.service;

import cz.devforce.partnersbootcamp.dto.common.UploadStatus;
import cz.devforce.partnersbootcamp.dto.common.UploadedFileFlag;
import cz.devforce.partnersbootcamp.dto.entity.FileDao;
import cz.devforce.partnersbootcamp.dto.mq.ProcessedFileMq;
import cz.devforce.partnersbootcamp.dto.mq.ProcessingFileMq;
import cz.devforce.partnersbootcamp.repository.FilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqConsumerProcessingService {

    private final FilesRepository filesRepository;

    private final RabbitMqProducerService rabbitMqProducerService;

    @RabbitListener(bindings = {
        @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.queue.file-processing}", durable = "true"),
            exchange = @Exchange(value = "${spring.rabbitmq.exchange}"),
            key = "${spring.rabbitmq.routing-key.file-processing}")
    })
    public void receivedFileForProcessing(ProcessingFileMq processingFileMq) {
        log.info("Received ProcessingFileMq from RabbitMq: " + processingFileMq.toString());
        if(processingFileMq.uploadedFileFlag() != UploadedFileFlag.CLEAN) {
            log.info("File is not clean, sorry :(");
            return;
        }
        // Obtain newly uploaded file
        var fileDao = filesRepository.findById(processingFileMq.id()).orElseThrow();
        // Perform validations
        validate(fileDao);
        // Perform some processing
        var processedFileDao = process(fileDao);
        // Update file with results
        var savedProcessedFileDao = filesRepository.save(processedFileDao);
        // Inform about file analysis status
        rabbitMqProducerService.sendFileProcessed(new ProcessedFileMq(
            savedProcessedFileDao.getId(),
            savedProcessedFileDao.getUploadedFileFlag(),
            savedProcessedFileDao.getUploadStatus()
        ));
    }

    private void validate(FileDao fileDao) {
        if(fileDao.getContent() == null || fileDao.getContent().length == 0) {
            throw new RuntimeException("File content is empty!");
        }
        if(fileDao.getUploadedFileFlag() != UploadedFileFlag.CLEAN) {
            throw new RuntimeException("File is not CLEAN!");
        }
        if(fileDao.getUploadStatus() != UploadStatus.PROCESSING) {
            throw new RuntimeException("File is not in PROCESSING state!");
        }
    }

    @SneakyThrows
    private FileDao process(FileDao fileDao) {
        Thread.sleep(5 * 1000);
        // Return processed file
        return new FileDao(
            fileDao.getId(),
            fileDao.getContent(),
            fileDao.getUploadedFileFlag(),
            UploadStatus.PROCESSED
        );
    }
}
