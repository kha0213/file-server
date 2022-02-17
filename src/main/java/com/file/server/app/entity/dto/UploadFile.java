package com.file.server.app.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadFile {
    // 업로드 파일 명
    private String uploadFileNm;
    // 저장 파일 명
    private String storageNm;
    // 저장 파일 위치
    private String storagePath;
    // 파일 사이즈
    private Long size;

    /**
     *
     * @param uploadFileNm 업로드 파일 명
     * @param saveFile 실제 저장한 파일 객체
     */
    public UploadFile(String uploadFileNm, File saveFile) throws IOException {
        this.uploadFileNm = uploadFileNm;
        this.storageNm = saveFile.getName();
        this.storagePath = saveFile.getCanonicalPath();
        this.size = saveFile.length();
    }
}
