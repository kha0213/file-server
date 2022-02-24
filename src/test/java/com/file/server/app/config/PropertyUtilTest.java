package com.file.server.app.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class PropertyUtilTest {

    @Test
    @DisplayName("globals.properties 테스트")
    public void propertyTest() {
        Resource resource = new ClassPathResource("globals.properties");
        try {
            Path path = Paths.get(resource.getURI());
            List<String> strings = Files.readAllLines(path);
            String s = strings.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // given

        // when

        // then
    }

}