package com.lsx.component.mqttBroker.mqtt.auto;


import com.lsx.component.mqttBroker.mqtt.bootstrap.BootstrapServer;
import com.lsx.component.mqttBroker.mqtt.bootstrap.NettyBootstrapServer;
import com.lsx.component.mqttBroker.mqtt.common.properties.InitBean;

public class InitServer {


    private InitBean serverBean;

    public InitServer(InitBean serverBean) {
        this.serverBean = serverBean;
    }

    BootstrapServer bootstrapServer;

    public void open(){
        if(serverBean!=null){
            bootstrapServer = new NettyBootstrapServer();
            bootstrapServer.setServerBean(serverBean);
            bootstrapServer.start();
        }
    }


    public void close(){
        if(bootstrapServer!=null){
            bootstrapServer.shutdown();
        }
    }

}
