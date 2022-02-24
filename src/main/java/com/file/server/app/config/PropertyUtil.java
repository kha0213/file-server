package com.file.server.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * java static final property 를 setting 해주는 util
 *
 */
@Slf4j
public class PropertyUtil {
    public static boolean initialize;
    public static Map<String, String> data;


    private static void init() {
        initialize = true;
        Resource resource = new ClassPathResource("globals.properties");
        try {
            Path path = Paths.get(resource.getURI());
            data = Files.readAllLines(path)
                    .stream()
                    .filter(line -> line.indexOf('=') != -1)
                    .collect(Collectors.toConcurrentMap(
                            line -> line.substring(0, line.indexOf("=")),
                            line -> line.substring(line.indexOf("=") + 1))
                    );
        } catch (IOException e) {
            log.error("init error ! {}", e.getMessage());
        }
    }

    public synchronized static String getProperty(String key) {
        if (!initialize) {
            init();
        }
        return data.get(key);
    }

    public synchronized static int getIntProperty(String key) {
        String result = getProperty(key);
        try {
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public synchronized static boolean getBooleanProperty(String key) {
        String result = getProperty(key);
        Set<String> trueSet = Set.of("true", "y");
        try {
            return trueSet.contains(result.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized static String getProperty(String key, String defaultProperty) {
        String result = getProperty(key);
        return StringUtils.hasText(result) ? result : defaultProperty;
    }

    public synchronized static boolean getBooleanProperty(String key, boolean defaultProperty) {
        String result = getProperty(key);
        return StringUtils.hasText(result) ? getBooleanProperty(key) : defaultProperty;
    }
}