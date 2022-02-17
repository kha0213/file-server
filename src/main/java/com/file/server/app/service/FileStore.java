package com.file.server.app.service;


import com.file.server.app.entity.dto.UploadFile;
import com.file.server.app.util.DirectoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.io.File.separator;

@Slf4j
@Component
public class FileStore {

    private String fileDir;

    public FileStore(@Value("${file.dir}") String fileDir) {
        this.fileDir = fileDir;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    /**
     * 파일을 실제 저장소에 저장하기
     * @param multipartFile Controller에서 넘어오는 MultipartFile객체
     * @return 저장 성공 시 UploadFile 객체 리턴
     * @throws IOException transferTo 에러시 IOException
     */
    public UploadFile storeFile(@NotNull MultipartFile multipartFile) throws IOException {
        String uploadFileNm = multipartFile.getName();
        String saveFileNm = UUID.randomUUID().toString();

        File saveDir = getOrCreateSavePath();
        File saveFile = new File(saveDir.getCanonicalPath() + separator + saveFileNm);

        multipartFile.transferTo(saveFile);

        return new UploadFile(uploadFileNm, saveFile);
    }

    /**
     * 파일 저장 경로 가져오기 (없으면 만들기)
     */
    private File getOrCreateSavePath() throws IOException {
        File file = new File(DirectoryUtils.addTodayDirectoryPath(fileDir));

        if(!file.exists()){
            Path path = file.toPath();
            Files.createDirectories(path);
        }

        return file;
    }

    /**
     * 파일 실제 삭제
     */
    public void deleteFiles(@NotEmpty List<File> files) {
        files.forEach(this::deleteFile);
    }

    /**
     * 파일 실제 삭제
     */
    public void deleteFile(@NotNull File file) {
        if ( !file.delete() ) {
            log.error("file delete fail! real filename [{}]", file.getName());
        }
    }
}
