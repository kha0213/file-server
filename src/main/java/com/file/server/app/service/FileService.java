package com.file.server.app.service;

import com.file.server.app.entity.File;
import com.file.server.app.entity.dto.FileInfo;
import com.file.server.app.entity.dto.UploadFile;
import com.file.server.app.entity.query.FileSearch;
import com.file.server.app.exception.NoSuchFileException;
import com.file.server.app.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FileService {
    private final FileRepository fileRepository;

    private final FileStore fileStore;

    /**
     * 파일을 실제로 저장 후 DB 에도 저장
     * @param uploadFile controller에서 넘어온 MultipartFile 리스트
     * @return List<Long> 저장된 파일의 키값들
     * @throws IOException IO에러
     */
    public Long upload(MultipartFile uploadFile) throws IOException, InvalidKeyException {
        UploadFile storeFile = fileStore.storeFile(uploadFile);
        File file = fileRepository.save(new File(storeFile));
        return file.getId();
    }

    /**
     * 파일을 실제로 저장 후 DB 에도 저장
     * @param uploadFiles controller에서 넘어온 MultipartFile 리스트
     * @return List<Long> 저장된 파일의 키값들
     * @throws IOException IO에러
     */
    public List<Long> upload(List<MultipartFile> uploadFiles) throws IOException, InvalidKeyException {
        List<UploadFile> storeFiles = fileStore.storeFiles(uploadFiles);

        List<File> files = fileRepository.saveAll(storeFiles.stream()
                                                            .map(File::new)
                                                            .collect(toList()));
        return files.stream()
                .map(File::getId)
                .collect(toList());
    }

    /**
     * 파일을 실제로 삭제 후 DB 에도 삭제
     * @param fileId file의 pk
     */
    public void deleteById(Long fileId) throws NoSuchFileException {
        log.debug("deleteFile fileId [{}] ", fileId);
        Optional<File> file = fileRepository.findById(fileId);

        if(file.isEmpty()) {
            throw new NoSuchFileException(fileId);
        }

        fileStore.deleteFile(file.get().getRealFile());
        fileRepository.deleteById(fileId);
    }

    /**
     * 파일을 실제로 삭제 후 DB 에도 삭제
     * @param fileIds file의 pk list
     */
    public void deleteAllByIds(@NotEmpty List<Long> fileIds) {
        log.debug("deleteFiles fileIds [{}] ", fileIds);
        List<File> files = fileRepository.findAllById(fileIds);

        fileStore.deleteFiles(
                files.stream()
                     .map(File::getRealFile)
                     .collect(toList()));
        fileRepository.deleteAllById(fileIds);
    }

    /**
     * 파일 Id 리스트로 파일 정보 찾기
     * @param search 파일 검색 객체
     * @return FileVO 리스트
     */
    public Page<FileInfo> findAllFileInfo(FileSearch search, Pageable pageable) {
        Page<File> files = fileRepository.findFiles(search, pageable);
        return files.map(FileInfo::new);
    }

    /**
     * 단건 파일 정보 찾기
     * @param fileId 찾는 ID
     */
    public FileInfo findFileInfoById(Long fileId) throws NoSuchFileException {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new NoSuchFileException(fileId));
        return new FileInfo(file);
    }

    /**
     * 파일 ID 기반 파일 정보 찾기
     * @param fileIds 파일 Id
     */
    public List<FileInfo> findAllFileInfoByIds(List<Long> fileIds) {
        return fileRepository.findAllById(fileIds)
                .stream()
                .map(FileInfo::new)
                .collect(Collectors.toList());
    }

    /**
     * 실제 File 객체 가져오는 메서드 (내용 복호화 전)
     * @param fileId 파일 key
     * @return 실제 File 객체
     * @throws NoSuchFileException 파일 ID로 파일 못 찾을 시 에러
     */
    public Resource getFileData(Long fileId) throws NoSuchFileException, MalformedURLException, FileNotFoundException {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new NoSuchFileException(fileId));
        java.io.File realFile = new java.io.File(file.getStoragePath());
        return fileStore.getResource(realFile);
    }
}

