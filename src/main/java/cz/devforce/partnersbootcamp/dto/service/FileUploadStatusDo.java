package cz.devforce.partnersbootcamp.dto.service;

import cz.devforce.partnersbootcamp.dto.common.UploadStatus;

public record FileUploadStatusDo(
    Long id,
    UploadStatus uploadStatus
) {
}
