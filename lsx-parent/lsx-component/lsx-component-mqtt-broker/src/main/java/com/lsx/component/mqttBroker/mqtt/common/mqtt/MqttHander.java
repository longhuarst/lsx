package com.lsx.component.mqttBroker.mqtt.common.mqtt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Optional;

/**
 * mqtt协议处理器
 **/
public  abstract  class MqttHander extends SimpleChannelInboundHandler<MqttMessage> {

    Logger logger = LoggerFactory.getLogger(MqttHander.class);

    MqttHandlerIntf mqttHandlerApi;

    public MqttHander(MqttHandlerIntf mqttHandlerIntf){
        this.mqttHandlerApi=mqttHandlerIntf;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) throws Exception {
        logger.info("channelRead0() msg = "+mqttMessage.toString());
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        Optional.ofNullable(mqttFixedHeader)
                .ifPresent(mqttFixedHeader1 -> doMessage(channelHandlerContext,mqttMessage));
    }

    public  abstract void doMessage(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage);




    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("【DefaultMqttHandler：channelInactive】"+ctx.channel().localAddress().toString()+"关闭成功");
        mqttHandlerApi.close(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            mqttHandlerApi.doTimeOut(ctx.channel(),(IdleStateEvent)evt);
        }
        super.userEventTriggered(ctx, evt);
    }


}
