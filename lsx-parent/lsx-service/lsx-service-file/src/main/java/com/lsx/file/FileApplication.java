package com.lsx.file;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication    //如果不需要数据库的话 可以使用 (exclude = DataSourceAutoConfiguration.class) //排除数据库
@EnableEurekaClient
public class FileApplication {


    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }

}
