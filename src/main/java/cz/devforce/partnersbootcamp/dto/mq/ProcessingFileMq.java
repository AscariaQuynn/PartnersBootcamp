package cz.devforce.partnersbootcamp.dto.mq;

import cz.devforce.partnersbootcamp.dto.common.UploadedFileFlag;

import java.util.Objects;

public record ProcessingFileMq(
    Long id,
    UploadedFileFlag uploadedFileFlag
) {
    public ProcessingFileMq {
        Objects.requireNonNull(id);
        Objects.requireNonNull(uploadedFileFlag);
    }
}
