package cz.devforce.partnersbootcamp.dto.mq;

import java.util.Objects;

public record AnalyzingFileMq(
    Long id
) {
    public AnalyzingFileMq {
        Objects.requireNonNull(id);
    }
}
