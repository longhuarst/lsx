package com.lsx.component.mqttBroker.mqtt.bootstrap.handler;

import com.lsx.component.mqttBroker.mqtt.bootstrap.ChannelService;
import com.lsx.component.mqttBroker.mqtt.bootstrap.bean.MqttChannel;
import com.lsx.component.mqttBroker.mqtt.common.exception.NoFindHandlerException;
import com.lsx.component.mqttBroker.mqtt.common.mqtt.MqttHander;
import com.lsx.component.mqttBroker.mqtt.common.mqtt.MqttHandlerIntf;
import com.lsx.component.mqttBroker.mqtt.common.mqtt.ServerMqttHandlerService;

//import com.lxr.iot.properties.ConnectOptions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 默认 mqtthandler处理
 **/

@ChannelHandler.Sharable
@Slf4j
@Component
public class DefaultMqttHandler extends MqttHander {

    Logger logger = LoggerFactory.getLogger(DefaultMqttHandler.class);

    private final MqttHandlerIntf mqttHandlerApi;

    @Autowired
    ChannelService channelService;


    public DefaultMqttHandler(MqttHandlerIntf mqttHandlerApi, MqttHandlerIntf mqttHandlerApi1) {
        super(mqttHandlerApi);
        this.mqttHandlerApi = mqttHandlerApi1;
    }

    @Override
    public void doMessage(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) {

        boolean debug = logger.isDebugEnabled();

        if (debug) logger.debug("doMessage()");


        Channel channel = channelHandlerContext.channel();
        ServerMqttHandlerService serverMqttHandlerService;
        if(mqttHandlerApi instanceof ServerMqttHandlerService){
            serverMqttHandlerService =(ServerMqttHandlerService)mqttHandlerApi;
        }
        else{
            throw new NoFindHandlerException("server handler 不匹配");
        }
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        if(mqttFixedHeader.messageType().equals(MqttMessageType.CONNECT)){
            if(!serverMqttHandlerService.login(channel, (MqttConnectMessage) mqttMessage)){
                channel.close();
                if (debug) logger.debug("connect 失败 关闭连接");
            }else{
                if (debug) logger.debug("connect 成功");
            }
            return ;
        }
        MqttChannel mqttChannel = channelService.getMqttChannel(channelService.getDeviceId(channel));
        if(mqttChannel!=null && mqttChannel.isLogin()){
            switch (mqttFixedHeader.messageType()){
                case PUBLISH:
                    serverMqttHandlerService.publish(channel, (MqttPublishMessage) mqttMessage);
                    break;
                case SUBSCRIBE:
                    serverMqttHandlerService.subscribe(channel, (MqttSubscribeMessage) mqttMessage);
                    break;
                case PINGREQ:
                    serverMqttHandlerService.pong(channel);
                    break;
                case DISCONNECT:
                    serverMqttHandlerService.disconnect(channel);
                    break;
                case UNSUBSCRIBE:
                    serverMqttHandlerService.unsubscribe(channel,(MqttUnsubscribeMessage)mqttMessage);
                    break;
                case PUBACK:
                    mqttHandlerApi.puback(channel,mqttMessage);
                    break;
                case PUBREC:
                    mqttHandlerApi.pubrec(channel,mqttMessage);
                    break;
                case PUBREL:
                    mqttHandlerApi.pubrel(channel,mqttMessage);
                    break;
                case PUBCOMP:
                    mqttHandlerApi.pubcomp(channel,mqttMessage);
                    break;
                default:
                    break;
            }
        }
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("【DefaultMqttHandler：channelActive】"+ctx.channel().remoteAddress().toString()+"链接成功");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exception",cause);
        mqttHandlerApi.close(ctx.channel());
    }
}
