package com.file.server;

import com.file.server.app.config.Property;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("Property.ENCRYPTION_TYPE = " + Property.ENCRYPTION_TYPE);
        System.out.println("Property.ENCRYPTION_APPLY = " + Property.ENCRYPTION_APPLY);
        System.out.println("Property.FILE_ROOT_DIR = " + Property.FILE_ROOT_DIR);
        System.out.println("Property.DIRECTORY_POLICY = " + Property.FILE_DIRECTORY_POLICY);
    }

}
