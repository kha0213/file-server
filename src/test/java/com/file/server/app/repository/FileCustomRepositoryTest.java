package com.file.server.app.repository;

import com.file.server.app.entity.File;
import com.file.server.app.entity.query.FileSearch;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
@Import({JpaTestConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class FileCustomRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private FileRepository fileRepository;
    
    @Test
    @DisplayName("findFiles 테스트")
    public void findFiles() {
        // given
        file100Insert();
        FileSearch search = new FileSearch();
        search.setMinSize(300L);
        search.setMaxSize(500L);
        search.setFileName("6");
        // when
        Page<File> files = fileRepository.findFiles(search, PageRequest.of(0, 100));
        files.stream().forEach(System.out::println);
        // then

        Assertions.assertThat(files.stream().allMatch(file -> file.getSize() >= 300L)).isTrue();
        Assertions.assertThat(files.stream().allMatch(file -> file.getSize() <= 500L)).isTrue();
        Assertions.assertThat(files.stream().allMatch(file -> file.getOriginNm().contains("6"))).isTrue();
    }

    private void file100Insert() {
        for (int i = 0; i < 100; i++) {
            File file = new File("origin"+i,"storage"+ i , "stroagePath"+i, 100 + (i % 10) * 100L);
            fileRepository.save(file);
        }
    }
}