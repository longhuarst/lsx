package com.lsx.component.mqttBroker.mqtt.bootstrap;

import com.lsx.component.mqttBroker.mqtt.common.properties.InitBean;
import io.netty.channel.Channel;

/**
 * 启动类接口
 **/
public interface BootstrapServer {

    void shutdown();

    void setServerBean(InitBean serverBean);

    void start();


}
