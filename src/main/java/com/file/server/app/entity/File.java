package com.file.server.app.entity;

import com.file.server.app.entity.dto.UploadFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;

import javax.persistence.*;

import static org.springframework.util.StringUtils.getFilenameExtension;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@TableGenerator(
        name = "FILE_SEQ_GENERATOR",
        table = "TB_SEQUENCES",
        pkColumnValue = "FILE_SEQ",
        allocationSize = 1)
@Entity
public class File extends BaseEntity {
    @Id
    @GeneratedValue(generator = "FILE_SEQ_GENERATOR", strategy = GenerationType.TABLE)
    @Column(name = "file_id", nullable = false)
    private Long id;

    // 업로드 파일 명
    private String uploadNm;
    // 저장 파일 명
    private String storageNm;
    /**
     *  저장 파일 위치
     *  root(file.dir) / 파일 타입 / 년 / 월 / 일 /
     *  ex ) 2022년 1월 14일 test.jpg -> root/IMAGE/2022/01/14/
     */
    private String storagePath;

    // 파일 타입 (IMAGE, VIDEO, ATTACH)
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    // 파일 확장자
    private String extensions;

    //파일 사이즈
    private Long size;

    public File(String uploadNm, long size) {
        this.uploadNm = uploadNm;
        this.size = size;
    }

    public File(UploadFile uploadFile) {
        Assert.notNull(uploadFile, "File entity constructor Error!!!");
        this.uploadNm = uploadFile.getUploadFileNm();
        this.storageNm = uploadFile.getStorageNm();
        this.storagePath = uploadFile.getStoragePath();
        this.size = uploadFile.getSize();
        String filenameExtension = getFilenameExtension(this.uploadNm);
        this.extensions = filenameExtension != null ? filenameExtension.toLowerCase() : null;
        this.fileType = FileType.getFileType(extensions);
    }

    public java.io.File getRealFile() {
        return new java.io.File(this.getStoragePath());
    }

    public File(String uploadNm, String storageNm, String storagePath, Long size) {
        this.uploadNm = uploadNm;
        this.storageNm = storageNm;
        this.storagePath = storagePath;
        String filenameExtension = getFilenameExtension(this.uploadNm);
        this.extensions = filenameExtension != null ? filenameExtension.toLowerCase() : null;
        this.fileType = FileType.getFileType(extensions);
        this.size = size;
    }
}

