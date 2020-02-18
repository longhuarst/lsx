package com.lsx.service;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootTest
public class ResourceTest {

    @Test
    public void testPublicKeyFile(){
        ClassPathResource classPathResource = new ClassPathResource("public.key");

        try {
            System.out.println(classPathResource.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
