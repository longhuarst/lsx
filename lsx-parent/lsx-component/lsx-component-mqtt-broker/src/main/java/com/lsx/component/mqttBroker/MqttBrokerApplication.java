package com.lsx.component.mqttBroker;


import com.lsx.component.mqttBroker.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@SpringBootApplication
public class MqttBrokerApplication {


    public static void main(String[] args) {
        SpringApplication.run(MqttBrokerApplication.class, args);

        //启动netty 服务
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(new InetSocketAddress(19000));
    }

}
