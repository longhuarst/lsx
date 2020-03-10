package com.lsx.service;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lsx.service.mapper")
public class TodolistApplication {


    public static void main(String[] args) {
        SpringApplication.run(TodolistApplication.class, args);
    }
}
