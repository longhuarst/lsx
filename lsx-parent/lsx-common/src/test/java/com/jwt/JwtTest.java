package com.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {


    /*
    * 创建令牌
    * */
    @Test
    public void testCreateToken(){

        //构建jwt令牌对象
        JwtBuilder builder = Jwts.builder();
        builder.setIssuer("lsx");//颁发者
        builder.setIssuedAt(new Date());//颁发日期
        builder.setSubject("jwt令牌测试");//主题信息

        //自定义载荷信息
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("company","lsx");
        userInfo.put("address","lsx");
        builder.addClaims(userInfo);//添加载荷

        //过期时间
        builder.setExpiration(new Date(System.currentTimeMillis() + 3600*1000));//一小时后过期

        builder.signWith(SignatureAlgorithm.HS256, "lsx-lsx");//1.签名算法， 2。密钥[可能不能过短，]

        System.out.println(builder.compact());

    }

    /*
    * 解析令牌
    * */

    @Test
    public void testParseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsc3giLCJpYXQiOjE1ODE3NzQ0ODEsInN1YiI6Imp3dOS7pOeJjOa1i-ivlSJ9.FbHuDfgBqxR912ukMxlYRkqhmjmGfelp8_p_sUSvqJc";
        Claims claims = Jwts.parser()
                .setSigningKey("lsx-lsx")   //密钥
                .parseClaimsJws(token) //要解析的令牌
                .getBody();//解析后的数据

        System.out.println(claims);
    }






}
