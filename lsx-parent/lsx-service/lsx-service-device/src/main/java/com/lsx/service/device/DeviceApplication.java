package com.lsx.service.device;


import com.lsx.service.device.configure.DefaultDeviceTypeConfig;
import com.lsx.service.device.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableJpaAuditing
public class DeviceApplication {

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }


    @Autowired
    private RedisTemplate<String, String> redisTemplate;




    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class, args);

        //启动服务端


        NettyServer nettyServer = new NettyServer();
        nettyServer.start();





    }


//
//    @Bean
//    public DefaultDeviceTypeConfig defaultDeviceTypeConfig(){
//        return new DefaultDeviceTypeConfig();
//    }
}
