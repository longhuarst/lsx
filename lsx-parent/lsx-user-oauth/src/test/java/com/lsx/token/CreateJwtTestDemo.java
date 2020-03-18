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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


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
    public String Test(){

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

        return token;
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
    public void TestDecode(String token) {
//        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODM4MzkyNDgsInVzZXJfbmFtZSI6ImxvbmdodWEiLCJhdXRob3JpdGllcyI6WyJhZG1pbiJdLCJqdGkiOiI5Y2M3NmM1NC02OTZhLTRlYjctOTkwZi0zNmJjZWJiMWEzZmYiLCJjbGllbnRfaWQiOiJsc3giLCJzY29wZSI6WyJhcHAiLCJhbGwiLCJVU0VSIl19.ilabzc1edAbSk9c83OzezXH0IURKoWh66dt6-ofDYj8M1l-qAVjtF_g4lqnmknKSYY30Aa0ztVEpaNM5H9FWTr5euBwTawt-aVHkbMH3CVRXHmytDd5EBvVqCgjLa-xGj1xm6iCYXzhN8j3HfB1VmS2NPmeYYfIB6KpA3U-EiXBQpFl6TNqmGeBGPyOI0rAPGdO3wW_iws__CK540ezPMwBkVFUp0lLJL8ABnniy_91yWZbVbAmzfc4YCiyR3LwiItweJfSK-PEUnm8_eO8x5y7kHCiCeMEKSxATBCRLXAGDy7uhSGn2DnSJFPgR2HPOV6gDkc2jCL3RiWeShlNQvQ";
        String publicKey = getPublicKey();

        Jwt jwt = JwtHelper.decodeAndVerify(
                token,
                new RsaVerifier(publicKey)
        );

        String claims = jwt.getClaims();
        System.out.println(claims);
    }


    @Test
    public void testEncoderAndDecoder(){

        String token1 = Test();

        TestDecode(token1);

        String token2 = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODM4MzkyNDgsInVzZXJfbmFtZSI6ImxvbmdodWEiLCJhdXRob3JpdGllcyI6WyJhZG1pbiJdLCJqdGkiOiI5Y2M3NmM1NC02OTZhLTRlYjctOTkwZi0zNmJjZWJiMWEzZmYiLCJjbGllbnRfaWQiOiJsc3giLCJzY29wZSI6WyJhcHAiLCJhbGwiLCJVU0VSIl19.ilabzc1edAbSk9c83OzezXH0IURKoWh66dt6-ofDYj8M1l-qAVjtF_g4lqnmknKSYY30Aa0ztVEpaNM5H9FWTr5euBwTawt-aVHkbMH3CVRXHmytDd5EBvVqCgjLa-xGj1xm6iCYXzhN8j3HfB1VmS2NPmeYYfIB6KpA3U-EiXBQpFl6TNqmGeBGPyOI0rAPGdO3wW_iws__CK540ezPMwBkVFUp0lLJL8ABnniy_91yWZbVbAmzfc4YCiyR3LwiItweJfSK-PEUnm8_eO8x5y7kHCiCeMEKSxATBCRLXAGDy7uhSGn2DnSJFPgR2HPOV6gDkc2jCL3RiWeShlNQvQ";

        TestDecode(token2);


        String token3 = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODQ1MTY0NDYsInVzZXJfbmFtZSI6ImxvbmdodWEiLCJhdXRob3JpdGllcyI6WyJhZG1pbiIsInVzZXIiXSwianRpIjoiYWVmNjY3MjItMjczZi00MWMzLTgwZjItYjgxYTc2MDc4YmMyIiwiY2xpZW50X2lkIjoibHN4Iiwic2NvcGUiOlsiYXBwIiwiYWxsIiwiVVNFUiJdfQ.K2piBwbSC3i0mD7EQqzTyot9AUZA0gfcsTxZNT_cXcvYzlDa6t_GhhW5p1W5AE2ha6HmwT5WhBT8bsYDh5wkFK6YUmT-4Nj1pgGwof-rn0W5lShUgoZW_x_77Q-1QmDFOEcFSTlgDOnHcmmx8_E72Qkk855AjiOXcNtWXfRGropZ0KzjFFmAii3a42rKbbXi1a4OAAGGASPNTUZbutUrPOWTZgE25yf5XbEpwiYBxXAqGqtBfpXmrJ9kJdkDm8xF8hegVT4ohydK14i40Gtyik9gvNOC8xhFUABNmcTVCXgeLGEf2GJW4Gt32vgI2n2IV76deduQyZJJj4r1mczsaA";

        TestDecode(token3);



    }


    @Test
    public void testDecodeBase64() throws Exception{
        //basic
        String str = "";
        byte[] decode = Base64.getDecoder().decode(str);
        String decodeStr = new String(decode, "UTF-8");

        System.out.println(decodeStr);


        

    }

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


}
