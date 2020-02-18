package com.lsx.token;

import com.alibaba.fastjson.JSON;

import com.lsx.oauth.OAuthApplication;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.IOException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


//SpringBootTest 一定要写， 不然Value注解无法获取  不指定classes 会出错，不知原因
@SpringBootTest(classes = OAuthApplication.class)
public class CreateJwtTestDemo {


    //从配置文件中加载数据
    @Value("${encrypt.key-store.password}")
    private String password;

    @Value("${encrypt.key-store.alias}")
    private String alias;

    @Value("${lstest}")
    private String lstest;

    @Test
    public void Test(){

        //加载证书 读取类目录下的文件
        ClassPathResource classPathResource = new ClassPathResource("lsx.jks");

        //读取证书数据， 加载证书数据
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, password.toCharArray());

        //获取证书中的一堆 私钥和公钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,password.toCharArray());

        //获取 私钥 -> RSA 算法
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        //创建令牌  需要私钥 + 盐 【rsa算法】
        Map<String, Object> payload = new HashMap<>();
        payload.put("nikename","tomcat");
        payload.put("address","cs");
        payload.put("role", "admin,user");

        Jwt jwt = JwtHelper.encode(JSON.toJSONString(payload), new RsaSigner(privateKey));

        //获取令牌
        String token = jwt.getEncoded();

        System.out.println(token);
    }



    @Test
    public void testPublicKeyFile(){
        ClassPathResource classPathResource = new ClassPathResource("public.key");

        try {
            System.out.println(classPathResource.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    * 解析令牌
    * */
    @Test
    public void TestDecode() {
        String token = "";
        String publicKey = "";

        Jwt jwt = JwtHelper.decodeAndVerify(
                token,
                new RsaVerifier(publicKey)
        );

        String claims = jwt.getClaims();
        System.out.println(claims);
    }


    @Test
    public void testDecodeBase64() throws Exception{
        //basic
        String str = "";
        byte[] decode = Base64.getDecoder().decode(str);
        String decodeStr = new String(decode, "UTF-8");

        System.out.println(decodeStr);


        

    }

}
