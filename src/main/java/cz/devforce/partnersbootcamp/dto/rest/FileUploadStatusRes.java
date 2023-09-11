package cz.devforce.partnersbootcamp.dto.rest;

import cz.devforce.partnersbootcamp.dto.common.UploadStatus;

public record FileUploadStatusRes(
    Long id,
    UploadStatus uploadStatus
) {
}
