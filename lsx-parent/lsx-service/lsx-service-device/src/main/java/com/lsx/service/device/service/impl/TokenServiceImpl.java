package com.lsx.service.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.lsx.service.device.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;



@Service
public class TokenServiceImpl implements TokenService {



    //从配置文件中加载数据
    @Value("${encrypt.key-store.password}")
    private String password;

    @Value("${encrypt.key-store.alias}")
    private String alias;



    //生成token

    public String getToken(String username, String uuid, long exp){
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
//        payload.put("nikename","tomcat");
//        payload.put("address","cs");
//        payload.put("role", "admin,user");
        payload.put("username", username);
        payload.put("uuid",uuid);
        payload.put("exp", exp);
        payload.put("authorities","device");//固定的设备权限

        Jwt jwt = JwtHelper.encode(JSON.toJSONString(payload), new RsaSigner(privateKey));

        //获取令牌
        String token = jwt.getEncoded();

        System.out.println(token);

        return token;
    }





}
