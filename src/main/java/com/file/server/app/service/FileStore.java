package com.file.server.app.service;


import com.file.server.app.entity.dto.UploadFile;
import com.file.server.app.util.DirectoryUtils;
import com.file.server.app.util.EncryptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.io.File.separator;

@Slf4j
@Component
public class FileStore {

    private final EncryptionUtils encryptionUtils;

    private String fileDir;

    @Autowired
    public FileStore(EncryptionUtils encryptionUtils, @Value("${file.dir}") String fileDir) {
        this.encryptionUtils = encryptionUtils;
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
        String uploadFileNm = multipartFile.getOriginalFilename();
        String saveFileNm = UUID.randomUUID().toString();

        File saveDir = getOrCreateSavePath();
        File saveFile = new File(saveDir.getCanonicalPath() + separator + saveFileNm);

        encryptTransferTo(multipartFile, saveFile);

        return new UploadFile(uploadFileNm, saveFile);
    }

    /**
     * 
     * @param multipartFile 업로드 한 multipartFile
     * @param saveFile 암호화 후 저장할 파일 객체
     */
    private void encryptTransferTo(MultipartFile multipartFile, File saveFile) {
        try {
            if (!saveFile.createNewFile()) {
                log.info("already exist file filename : [{}]", saveFile.getName());
            }
            encryptionUtils.encryptContentSaveFile(multipartFile.getBytes(), saveFile);
        } catch (IOException e) {
            log.error("IOException [{}]", e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("InvalidKeyException [{}]", e.getMessage());
        }
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
