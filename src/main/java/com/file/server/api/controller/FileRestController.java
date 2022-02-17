package com.file.server.api.controller;

import com.file.server.api.common.SuccessResponse;
import com.file.server.app.entity.dto.FileDto;
import com.file.server.app.entity.query.FileSearch;
import com.file.server.app.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/file")
public class FileRestController {
    private final FileService fileService;

    @GetMapping("/list")
    public SuccessResponse<Page<FileDto>> list(FileSearch fileSearch, Pageable pageable) {
        Page<FileDto> files = fileService.findFiles(fileSearch, pageable);
        return new SuccessResponse<>(files);
    }

    @GetMapping("/{fileId}")
    public SuccessResponse<FileDto> findOne(@PathVariable Long fileId) throws Exception {
        FileDto file = fileService.find(fileId);
        return new SuccessResponse<>(file);
    }

    @PostMapping("/upload")
    public SuccessResponse<List<FileDto>> upload(@Valid @NotEmpty List<MultipartFile> file) throws IOException {
        List<Long> uploadIds = fileService.upload(file);
        List<FileDto> uploadFiles = fileService.findAllById(uploadIds);
        return new SuccessResponse<>(uploadFiles);
    }

    @PostMapping("/delete")
    public SuccessResponse delete(@RequestParam @NotEmpty List<Long> fileId)  {
        fileService.deleteFiles(fileId);
        return new SuccessResponse();
    }
    @PostMapping("/delete/{fileId}")
    public SuccessResponse delete(@PathVariable Long fileId) throws Exception {
        fileService.delete(fileId);
        return new SuccessResponse();
    }
}
