package com.lsx.service;


import com.lsx.service.dao.UserRespository;
import com.lsx.service.entity.User;
import entity.Result;
import entity.StatusCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.UUID;

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


    @Autowired
    private UserRespository userRespository;

    @Test
    void createUser(){
        User user = new User();

        //暂时不使用加密
        //使用加密
//        System.out.println();
        user.setPassword(new BCryptPasswordEncoder().encode("longhua").toString());
        user.setUsername("longhua");

        String uuid = UUID.randomUUID().toString();

        user.setUuid(uuid);

        user.setRole("admin");


        User resUser = null;
        try{
            resUser = userRespository.save(user);
        }catch (Exception e){
            e.printStackTrace();
//            return new Result(false, StatusCode.ERROR, "注册失败");
            System.out.println("failed");
        }

//        return new Result(true, StatusCode.OK, "注册成功", resUser);
        System.out.println("success");

    }

}
