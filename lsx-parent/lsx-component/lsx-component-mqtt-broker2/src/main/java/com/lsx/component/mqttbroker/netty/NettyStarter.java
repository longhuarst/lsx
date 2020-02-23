package com.lsx.component.mqttbroker.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class NettyStarter implements CommandLineRunner {

    @Autowired
    private NettyServer server;

    @Override
    public void run(String... args) throws Exception {
        server.start();
    }
}
