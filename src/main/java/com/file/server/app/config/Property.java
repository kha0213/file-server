package com.file.server.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import static com.file.server.app.config.PropertyUtil.getBooleanProperty;
import static com.file.server.app.config.PropertyUtil.getProperty;

/**
 * globals.properties 를 java static 변수로 세팅해줌
 */

@Getter
@Setter
@Component
public class Property {
    // 루트 DIR
    public static String FILE_ROOT_DIR = getProperty("file.root.dir");
    // 파일 생성시 디렉터리 추가하는 규칙
    public static final String FILE_DIRECTORY_POLICY = getProperty("file.directory.policy", "YYYY/MM/DD");

    // 암호화 방식
    public static final String ENCRYPTION_TYPE = getProperty("encryption.type", "AES");
    // 암호화 적용여부
    public static final boolean ENCRYPTION_APPLY = getBooleanProperty("encryption.apply", false);
    // 암호화 변환
    public static final String ENCRYPTION_TRANSFORMATION = getProperty("encryption.transformation", "AES/CBC/PKCS5Padding");

    // 유저 기본 이름
    public static final String USER_DEFAULT_NAME = getProperty("user.default.name", "SYSTEM");

}
