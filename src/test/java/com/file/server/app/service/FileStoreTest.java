package com.file.server.app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileStoreTest {
    @Mock
    private MultipartFile multipartFile;

    private FileStore fileStore = new FileStore("C:\\Long\\test");

    @Test
    @DisplayName("파일 저장소 파일 저장 테스트")
    void storeFileTest() throws IOException {

        //given
        when(multipartFile.getOriginalFilename()).thenReturn("1.txt");
        doNothing().when(multipartFile).transferTo((java.io.File) any());

        //when
        File file = fileStore.storeFile(multipartFile);

        assertThat(file.getParentFile().isDirectory()).isTrue();
        assertThat(file.getParentFile().getName()).isEqualTo(String.valueOf(LocalDate.now().getDayOfMonth()));
    }

    @Test
    @DisplayName("파일 삭제 테스트")
    void deleteTest() throws IOException {

        //given
        File file = new File("C:\\Long\\test\\1.txt");
        if(!file.exists()) {
            createFile(file);
        }

        fileStore.deleteFile(file);
    }

    @Test
    @DisplayName("파일 리스트 삭제 테스트")
    void deleteListTest() throws IOException {

        //given
        List<File> listFiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            File file = new File("C:\\Long\\test\\" + i + ".txt");
            listFiles.add(file);
            if(!file.exists()) {
                createFile(file);
            }
        }

        //when
        fileStore.deleteFiles(listFiles);

        Set<String> fileNameSet = listFiles.stream()
                .map(File::getName)
                .collect(toSet());
        //then
        File[] files = new File("C:\\Long\\test\\").
                listFiles(file -> fileNameSet.contains(file.getName()));
        assert files != null;
        assertThat(files.length).isEqualTo(0);
    }

    private void createFile(File file) throws IOException {
        File parentFile = file.getParentFile();
        if(!parentFile.exists()) {
            Files.createDirectories(parentFile.toPath());
        }
        inputTestText(file);
    }

    private void inputTestText(File file) {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
            bf.write("테스트입니다.");
            bf.flush();
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }
    }
}