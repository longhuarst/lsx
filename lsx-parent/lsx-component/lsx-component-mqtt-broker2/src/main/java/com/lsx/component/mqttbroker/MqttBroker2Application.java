package com.lsx.component.mqttbroker;


import com.lsx.component.mqttbroker.bean.SpringBean;
import com.lsx.component.mqttbroker.netty.NettyServer;
import com.lsx.component.mqttbroker.netty.ServerConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MqttBroker2Application {


    public static void main(String[] args) {

        SpringApplication.run(MqttBroker2Application.class, args);
    }




    @Bean
    public SpringBean springBean(){
        return new SpringBean();
    }

}
