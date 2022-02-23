package com.file.server.app.entity.dto;

import com.file.server.app.entity.File;
import com.file.server.app.entity.FileType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 외부에 리턴할 fileDto
 */
@Data
@NoArgsConstructor
public class FileDto {
    // 파일 id
    private Long id;

    // 업로드 파일 명
    private String name;

    // 파일 타입 (IMAGE, VIDEO, ATTACH)
    private FileType fileType;

    // 파일 확장자
    private String extensions;

    // 파일 사이즈
    private Long size;

    // 파일 생성일
    private LocalDateTime createdDate;

    // 파일 생성 유저
    private String createBy;

    // 실제 파일 객체
    private java.io.File realFile;

    public FileDto(File file) {
        id = file.getId();
        name = file.getUploadNm();
        fileType = file.getFileType();
        extensions = file.getExtensions();
        size = file.getSize();
        createdDate = file.getCreatedDate();
        createBy = file.getCreateBy();
        realFile = new java.io.File(file.getStoragePath());
    }
}
