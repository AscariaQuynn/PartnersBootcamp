package cz.devforce.partnersbootcamp.service;

import cz.devforce.partnersbootcamp.dto.common.UploadStatus;
import cz.devforce.partnersbootcamp.dto.common.UploadedFileFlag;
import cz.devforce.partnersbootcamp.dto.entity.FileDao;
import cz.devforce.partnersbootcamp.dto.mq.ProcessingFileMq;
import cz.devforce.partnersbootcamp.dto.mq.AnalyzingFileMq;
import cz.devforce.partnersbootcamp.repository.FilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqConsumerAnalyzeService {

    private final FilesRepository filesRepository;

    private final AntivirusService antivirusService;

    private final RabbitMqProducerService rabbitMqProducerService;

    @RabbitListener(bindings = {
        @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.queue.file-uploaded}", durable = "true"),
            exchange = @Exchange(value = "${spring.rabbitmq.exchange}"),
            key = "${spring.rabbitmq.routing-key.file-uploaded}")
    })
    public void receivedAnalyzingFileMq(AnalyzingFileMq analyzingFileMq) {
        log.info("Received AnalyzingFileMq from RabbitMq: " + analyzingFileMq.toString());
        // Obtain newly uploaded file
        var fileDao = filesRepository.findById(analyzingFileMq.id()).orElseThrow();
        // Perform validations
        validate(fileDao);
        // Perform analysis and modifications
        var analyzedFileDao = analyzeAndModify(fileDao);
        var savedAnalyzedFileDao = filesRepository.save(analyzedFileDao);
        // Inform about file analysis status, even about termination in case someone is interested
        rabbitMqProducerService.sendFileProcessing(new ProcessingFileMq(
            savedAnalyzedFileDao.getId(),
            savedAnalyzedFileDao.getUploadedFileFlag()
        ));
    }

    private void validate(FileDao fileDao) {
        if(fileDao.getContent() == null || fileDao.getContent().length == 0) {
            throw new RuntimeException("File content is empty!");
        }
        if(fileDao.getUploadStatus() != UploadStatus.ANALYZING) {
            throw new RuntimeException("File is not in ANALYZING state!");
        }
    }

    private FileDao analyzeAndModify(FileDao fileDao) {
        // Analyze with antivirus
        var hasVirus = antivirusService.analyze(fileDao.getContent());
        // Modify and update file
        return new FileDao(
            fileDao.getId(),
            hasVirus ? new byte[0] : fileDao.getContent(),
            hasVirus ? UploadedFileFlag.HAS_VIRUS : UploadedFileFlag.CLEAN,
            hasVirus ? UploadStatus.TERMINATED : UploadStatus.PROCESSING
        );
    }
}
