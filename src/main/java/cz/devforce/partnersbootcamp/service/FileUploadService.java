package cz.devforce.partnersbootcamp.service;

import cz.devforce.partnersbootcamp.dto.common.UploadStatus;
import cz.devforce.partnersbootcamp.dto.common.UploadedFileFlag;
import cz.devforce.partnersbootcamp.dto.entity.FileDao;
import cz.devforce.partnersbootcamp.dto.mq.AnalyzingFileMq;
import cz.devforce.partnersbootcamp.dto.service.FileUploadStatusDo;
import cz.devforce.partnersbootcamp.repository.FilesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FilesRepository filesRepository;

    private final RabbitMqProducerService rabbitMqProducerService;

    public FileUploadStatusDo uploadFile(byte[] file) {
        // Save file into database
        var savedFileDao = filesRepository.save(new FileDao(
            file,
            UploadedFileFlag.UNDECIDED,
            UploadStatus.ANALYZING
        ));
        // Broadcast that file is available for further analyzing and processing
        rabbitMqProducerService.sendFileAnalyzing(new AnalyzingFileMq(savedFileDao.getId()));
        // Directly after file upload, return status in non-blocking way
        return new FileUploadStatusDo(
            savedFileDao.getId(),
            savedFileDao.getUploadStatus()
        );
    }

    public FileUploadStatusDo getUploadFileStatus(Long id) {
        var fileDao = filesRepository.findById(id).orElse(null);
        return new FileUploadStatusDo(
            fileDao != null ? fileDao.getId() : null,
            fileDao != null ? fileDao.getUploadStatus() : null
        );
    }
}
