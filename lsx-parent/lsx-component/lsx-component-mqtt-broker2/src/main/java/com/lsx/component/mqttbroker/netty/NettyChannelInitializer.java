package com.lsx.component.mqttbroker.netty;


import com.lsx.component.mqttbroker.bean.SpringBean;
import com.lsx.component.mqttbroker.mqtt.coder.MqttEncoder;
import com.lsx.component.mqttbroker.mqtt.coder.MqttDecoder;
import com.lsx.component.mqttbroker.mqtt.handler.MqttHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;


//不再spring线程中 无法使用 Spring的注入
//使用 SpringBean 获取
public class NettyChannelInitializer extends ChannelInitializer {


    private ServerConfigure configure = SpringBean.getBean(ServerConfigure.class);//多线程 获取bean

    private MqttHandler mqttHandler = SpringBean.getBean(MqttHandler.class);//多线程 获取bean


    @Override
    protected void initChannel(Channel ch) throws Exception {




        //MQTT
        // 由于 netty 自带的mqtt 编解码器   只能够匹配 23 位的 client id  自己修改替换成 32bit 的
        ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
        ch.pipeline().addLast("decoder", new MqttDecoder());

        ch.pipeline().addLast(new IdleStateHandler(configure.getHeart(), 0 , 0));//超时时间
        ch.pipeline().addLast(SpringBean.getBean(MqttHandler.class));
    }
}
