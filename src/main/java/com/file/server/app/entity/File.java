package com.file.server.app.entity;

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
    private String originNm;
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

    public File(String originNm, long size) {
        this.originNm = originNm;
        this.size = size;
    }

    public File(java.io.File realFile) {
        Assert.notNull(realFile, "File entity constructor Error!!!");
        this.storageNm = realFile.getName();
        this.storagePath = realFile.getAbsolutePath();
        this.extensions = getFilenameExtension(storagePath);
        this.size = realFile.length();
        this.fileType = FileType.getFileType(extensions);
    }

    public java.io.File getRealFile() {
        return new java.io.File(this.getStoragePath() + java.io.File.separator + this.getStorageNm());
    }
}

