package com.file.server.api.controller;

import com.file.server.api.common.Result;
import com.file.server.app.entity.dto.FileInfo;
import com.file.server.app.entity.query.FileSearch;
import com.file.server.app.exception.NoSuchFileException;
import com.file.server.app.service.FileService;
import com.file.server.app.util.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static com.file.server.api.common.Result.success;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/file")
public class FileRestController {
    private final FileService fileService;
    private final EncryptionUtils encryptionUtils;

    @GetMapping("/list")
    public Result<Page<FileInfo>> list(FileSearch fileSearch, Pageable pageable) {
        Page<FileInfo> files = fileService.findAllFileInfo(fileSearch, pageable);
        return new Result<>(files);
    }

    @GetMapping("/{fileId}")
    public Result<FileInfo> findOne(@PathVariable Long fileId) throws Exception {
        FileInfo file = fileService.findFileInfoById(fileId);
        return new Result<>(file);
    }

    /**
     * 파일 실제 다운로드
     * @param fileId fileId
     * @return ResponseEntity
     * @throws NoSuchFileException 파일 없을 때 발생
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId)
            throws NoSuchFileException, FileNotFoundException {
        File file = fileService.findRealFileById(fileId);
        FileInfo fileInfo = fileService.findFileInfoById(fileId);
        Resource resource = getResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileInfo.getName() + "\"")
                .body(resource);
    }


    @PostMapping("/upload")
    public Result<List<FileInfo>> upload(@Valid @NotEmpty List<MultipartFile> file) throws IOException {
        List<Long> uploadIds = fileService.upload(file);
        List<FileInfo> uploadFiles = fileService.findAllFileInfoByIds(uploadIds);
        return new Result<>(uploadFiles);
    }

    @PostMapping("/delete")
    public Result<Object> delete(@RequestParam @NotEmpty List<Long> fileId)  {
        fileService.deleteAllByIds(fileId);
        return success();
    }
    @PostMapping("/delete/{fileId}")
    public Result<Object> delete(@PathVariable Long fileId) throws Exception {
        fileService.deleteById(fileId);
        return success();
    }

    /**
     * 파일 Dto로 Resource 가져오기
     * @param file FileDto
     * @return resource
     */
    private Resource getResource(File file) throws FileNotFoundException {
        byte[] content = encryptionUtils.decryptContentByFile(file);
        Resource resource = new ByteArrayResource(content);
        if (!resource.exists()) {
            throw new FileNotFoundException();
        }
        return resource;
    }
}
