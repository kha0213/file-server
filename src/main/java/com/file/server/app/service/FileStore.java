package com.file.server.app.service;


import com.file.server.app.entity.dto.UploadFile;
import com.file.server.app.util.DirectoryUtils;
import com.file.server.app.util.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.file.server.app.config.Property.ENCRYPTION_APPLY;
import static com.file.server.app.config.Property.FILE_ROOT_DIR;
import static java.io.File.separator;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStore {

    private final EncryptionUtils encryptionUtils;

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException, InvalidKeyException {
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
     * @param multipartFile Controller 에서 넘어오는 MultipartFile 객체
     * @return 저장 성공 시 UploadFile 객체 리턴
     * @throws IOException transferTo 에러시 IOException
     */
    public UploadFile storeFile(@NotNull MultipartFile multipartFile) throws IOException, InvalidKeyException {
        String uploadFileNm = multipartFile.getOriginalFilename();
        String saveFileNm = UUID.randomUUID().toString();

        File saveDir = getOrCreateSavePath();
        File saveFile = new File(saveDir.getCanonicalPath() + separator + saveFileNm);

        transferTo(multipartFile, saveFile);

        return new UploadFile(uploadFileNm, saveFile);
    }

    /**
     * multipartFile 안의 file 을 saveFile 에 저장
     * @param multipartFile 업로드 한 multipartFile
     * @param saveFile 저장할 파일 객체
     */
    private void transferTo(MultipartFile multipartFile, File saveFile) throws IOException, InvalidKeyException {
        if (ENCRYPTION_APPLY) {
            if (!saveFile.createNewFile()) log.info("already exist file filename : [{}]", saveFile.getName());
            encryptionUtils.encryptContentSaveFile(multipartFile.getBytes(), saveFile);
        } else {
            multipartFile.transferTo(saveFile);
        }
    }

    /**
     * 파일 저장 경로 가져오기 (없으면 만들기)
     */
    private File getOrCreateSavePath() throws IOException {
        File file = new File(DirectoryUtils.addTodayDirectoryPath(FILE_ROOT_DIR));

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

    /**
     * 파일 Dto로 Resource 가져오기
     * @param file FileDto
     * @return resource
     */
    public Resource getResource(File file) throws FileNotFoundException, MalformedURLException {
        if(ENCRYPTION_APPLY) {
            byte[] content = encryptionUtils.decryptContentByFile(file);
            Resource resource = new ByteArrayResource(content);
            if (!resource.exists()) {
                throw new FileNotFoundException();
            }
            return resource;
        } else {
            return new UrlResource(file.toURI());
        }
    }
}
