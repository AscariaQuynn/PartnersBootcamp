package cz.devforce.partnersbootcamp.dto.mq;

import cz.devforce.partnersbootcamp.dto.common.UploadStatus;
import cz.devforce.partnersbootcamp.dto.common.UploadedFileFlag;

import java.util.Objects;

public record ProcessedFileMq(
    Long id,
    UploadedFileFlag uploadedFileFlag,

    UploadStatus uploadStatus
) {
    public ProcessedFileMq {
        Objects.requireNonNull(id);
        Objects.requireNonNull(uploadedFileFlag);
        Objects.requireNonNull(uploadStatus);
    }
}
