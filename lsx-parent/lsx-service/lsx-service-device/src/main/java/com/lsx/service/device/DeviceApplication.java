package com.lsx.service.device;


import com.lsx.service.device.configure.DefaultDeviceTypeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableJpaAuditing
public class DeviceApplication {

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }


    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class, args);

    }


//
//    @Bean
//    public DefaultDeviceTypeConfig defaultDeviceTypeConfig(){
//        return new DefaultDeviceTypeConfig();
//    }
}
