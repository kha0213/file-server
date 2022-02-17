package com.file.server.app.repository;

import com.file.server.app.entity.File;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
@Import({JpaTestConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class FileRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private FileRepository fileRepository;

    @Test
    @DisplayName("파일 저장 테스트")
    void fileSaveTest() {
        //given
        File file = new File();
        file.setOriginNm("test");

        //when
        File save = fileRepository.save(file);

        //then
        assertThat(file.getId()).isEqualTo(1L);
        assertThat(file).isEqualTo(save);
        assertThat(file).isSameAs(save);
    }

    @Test
    @DisplayName("findBySizeEquals 테스트")
    public void findBySizeEquals() {
        // given
        file10Insert();
        File file = new File("size100", 100);
        fileRepository.save(file);
        // when
        List<File> result = fileRepository.findBySizeEquals(100L);
        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getOriginNm()).isEqualTo("test1");
        assertThat(result.get(1).getOriginNm()).isEqualTo("size100");
    }

    private void file10Insert() {
        for (int i = 0; i < 10; i++) {
            File file = new File("test"+i, i * 100);
            fileRepository.save(file);
        }
    }

}