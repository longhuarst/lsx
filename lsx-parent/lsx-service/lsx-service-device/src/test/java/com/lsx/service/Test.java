package com.lsx.service;


import java.util.*;

public class Test {



    @org.junit.jupiter.api.Test
    void test(){
        System.out.println(new Date().getTime());

        //1583839248
        //1583823290144

        //

        Map<String, Object> payload = new HashMap<>();
//        payload.put("nikename","tomcat");
//        payload.put("address","cs");
//        payload.put("role", "admin,user");
        payload.put("user_name", "TEST");
        payload.put("uuid","TEST");
        payload.put("jti", "TEST");
        payload.put("exp", 123); //过期时间
        List<String> list = new ArrayList<>();
        list.add("device");
        payload.put("authorities",list);//固定的设备权限
        payload.put("role","device");

        System.out.println(payload.toString());

    }




}
