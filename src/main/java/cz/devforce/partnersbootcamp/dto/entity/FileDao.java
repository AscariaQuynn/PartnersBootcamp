package cz.devforce.partnersbootcamp.dto.entity;

import cz.devforce.partnersbootcamp.dto.common.UploadStatus;
import cz.devforce.partnersbootcamp.dto.common.UploadedFileFlag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class FileDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @SuppressWarnings("NullableProblems")
    @NonNull
    @Column(nullable = false)
    private byte[] content;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UploadedFileFlag uploadedFileFlag;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UploadStatus uploadStatus;
}
