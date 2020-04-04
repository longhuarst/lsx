package com.lsx.service.device.bean;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TokenValid {

    static Logger logger = LoggerFactory.getLogger(TokenValid.class);


    static String publicKey = null;

    /*
     * 获取非对称加密公钥 Key
     * */
    private String getPublicKey(){
        ClassPathResource classPathResource = new ClassPathResource("public.key");

        try {
            System.out.println(classPathResource.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    public boolean validToken(String token){

        if (publicKey == null) {
            publicKey = getPublicKey();
        }

        try {
            Jwt jwt = JwtHelper.decodeAndVerify(
                    token,
                    new RsaVerifier(publicKey)
            );

            String claims = jwt.getClaims();
            System.out.println(claims);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public String validDeviceToken(String token){
        if (publicKey == null) {
            publicKey = getPublicKey();
        }



        try {
            Jwt jwt = JwtHelper.decodeAndVerify(
                    token,
                    new RsaVerifier(publicKey)
            );

            String claims = jwt.getClaims();
            System.out.println(claims);




            JSONObject jsonObject = JSONObject.parseObject(claims);


            Map<String, Object> claimsMap = JSON.parseObject(claims, HashMap.class);

            List<String> roles = (List<String>) claimsMap.get("role");
            logger.info("roles="+roles);
            boolean ok = false;
            for (String role : roles){
                if (role.equals("device")){
                    ok = true;
                }
            }
            if (ok == false) {
                logger.info("role error");
                return null;
            }

            long exp = Long.valueOf(jsonObject.get("exp").toString());
            Date date = new Date();
            long now = date.getTime()/1000;

            if (now > exp){
                logger.info("exp");
                return null;//过期了
            }

            return (String) claimsMap.get("uuid");


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
