package com.lsx.component.mqttbroker.mqtt.handler;


import com.lsx.component.mqttbroker.mqtt.bean.MqttChannel;
import com.lsx.component.mqttbroker.mqtt.exception.NoFindHandlerException;
import com.lsx.component.mqttbroker.mqtt.service.ChannelService;
import com.lsx.component.mqttbroker.mqtt.service.MqttHandlerIntf;
import com.lsx.component.mqttbroker.mqtt.service.ServerMqttHandlerService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@ChannelHandler.Sharable
@Component
public class MqttHandler extends SimpleChannelInboundHandler<MqttMessage> {



    Logger logger = LoggerFactory.getLogger(MqttHandler.class);
    boolean debug = logger.isDebugEnabled();

    private final MqttHandlerIntf mqttHandlerApi; //

    @Autowired
    ChannelService channelService;//通道管理服务


    public MqttHandler(MqttHandlerIntf mqttHandlerApi){
        this.mqttHandlerApi = mqttHandlerApi;
    }





    public void doMessage(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage){

        //从上下文 获取 通道
        Channel channel = channelHandlerContext.channel();

        ServerMqttHandlerService serverMqttHandlerService; // 应答 输出管理服务
        if(mqttHandlerApi instanceof ServerMqttHandlerService){
            serverMqttHandlerService =(ServerMqttHandlerService)mqttHandlerApi;
        }
        else{
            throw new NoFindHandlerException("server handler 不匹配");
        }


        //固定头处理器
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();



        //MQTT 连接
        if (mqttFixedHeader.messageType().equals(MqttMessageType.CONNECT)){
            logger.info("客户端 连接 CONNECT");
            if (!serverMqttHandlerService.login(channel, (MqttConnectMessage) mqttMessage)){
                channel.close();//关闭
                logger.info("连接失败");
            }else{
                logger.info("连接成功");
            }
            return;
        }

        //根据 channel 获取 device id  在 由 device id 获取 mqtt channel
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
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {

        MqttFixedHeader mqttFixedHeader = msg.fixedHeader();
        Optional.ofNullable(mqttFixedHeader).ifPresent(mqttFixedHeader1 -> doMessage(ctx, msg));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //mqtt关闭
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            //mqtt关闭
            logger.info("客户端超时 关闭");
        }
        super.userEventTriggered(ctx, evt);
    }
}
