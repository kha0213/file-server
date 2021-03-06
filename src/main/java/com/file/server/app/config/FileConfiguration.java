package com.file.server.app.config;

import com.file.server.app.util.EncryptionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

import static com.file.server.app.config.Property.ENCRYPTION_TRANSFORMATION;
import static com.file.server.app.config.Property.ENCRYPTION_TYPE;

@Configuration
public class FileConfiguration {
    @Bean
    public CommonsMultipartResolver commonsMultipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        commonsMultipartResolver.setMaxUploadSize(1024 * 1024 * 1024); // 1gb
        commonsMultipartResolver.setMaxInMemorySize(1024 * 1024 * 1024);
        return commonsMultipartResolver;
    }

    @Bean
    public EncryptionUtils fileEncryptionUtils() throws NoSuchAlgorithmException, NoSuchPaddingException {
        SecretKey secretKey = KeyGenerator.getInstance(ENCRYPTION_TYPE).generateKey();
        return new EncryptionUtils(secretKey, ENCRYPTION_TRANSFORMATION);
    }

}
