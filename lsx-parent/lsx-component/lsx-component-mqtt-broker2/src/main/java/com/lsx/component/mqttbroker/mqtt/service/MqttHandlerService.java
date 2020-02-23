package com.lsx.component.mqttbroker.mqtt.service;

import com.lsx.component.mqttbroker.mqtt.enums.ConfirmStatus;
import com.lsx.component.mqttbroker.mqtt.bean.SendMqttMessage;
import io.micrometer.core.instrument.util.StringUtils;
import io.netty.channel.Channel;
//import io.netty.handler.codec.mqtt;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//消息处理service

@Component
public class MqttHandlerService extends ServerMqttHandlerService implements BaseApi {

    Logger logger = LoggerFactory.getLogger(MqttHandlerService.class);
    boolean debug = logger.isDebugEnabled();


    @Autowired
    ChannelService mqttChannelService;


    /**
     * 登录
     *
     */
    @Override
    public boolean login(Channel channel, MqttConnectMessage mqttConnectMessage) {
//        校验规则 自定义校验规则
        String deviceId = mqttConnectMessage.payload().clientIdentifier();
        if (StringUtils.isBlank(deviceId)) {
            return false;
        }
        return  Optional.ofNullable(mqttChannelService.getMqttChannel(deviceId))
                .map(mqttChannel -> {
                    switch (mqttChannel.getSessionStatus()){
                        case OPEN:
                            return false;
                    }
                    mqttChannelService.loginSuccess(channel, deviceId, mqttConnectMessage);
                    return true;
                }).orElseGet(() -> {
                    mqttChannelService.loginSuccess(channel, deviceId, mqttConnectMessage);
                    return  true;
                });

    }

    /**
     * 发布
     */
    @Override
    public void publish(Channel channel, MqttPublishMessage mqttPublishMessage) {
        mqttChannelService.publishSuccess(channel, mqttPublishMessage);
    }

    /**
     * 订阅
     */
    @Override
    public void subscribe(Channel channel, MqttSubscribeMessage mqttSubscribeMessage) {
        Set<String> topics = mqttSubscribeMessage.payload().topicSubscriptions().stream().map(mqttTopicSubscription ->
                mqttTopicSubscription.topicName()
        ).collect(Collectors.toSet());
        mqttChannelService.suscribeSuccess(mqttChannelService.getDeviceId(channel), topics);
        subBack(channel, mqttSubscribeMessage, topics.size());
    }

    private void subBack(Channel channel, MqttSubscribeMessage mqttSubscribeMessage, int num) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(mqttSubscribeMessage.variableHeader().messageId());
        List<Integer> grantedQoSLevels = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            grantedQoSLevels.add(mqttSubscribeMessage.payload().topicSubscriptions().get(i).qualityOfService().value());
        }
        MqttSubAckPayload payload = new MqttSubAckPayload(grantedQoSLevels);
        MqttSubAckMessage mqttSubAckMessage = new MqttSubAckMessage(mqttFixedHeader, variableHeader, payload);
        channel.writeAndFlush(mqttSubAckMessage);
    }


    /**
     * 关闭通道
     */
    @Override
    public void close(Channel channel) {
        mqttChannelService.closeSuccess(mqttChannelService.getDeviceId(channel), false);
        channel.close();
    }

    /**
     * 回复pong消息
     */
    @Override
    public void pong(Channel channel) {
        if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
            logger.info("收到来自：【" + channel.remoteAddress().toString() + "】心跳");
            MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0);
            channel.writeAndFlush(new MqttMessage(fixedHeader));
        }
    }

    /**
     * 取消订阅
     */
    @Override
    public void unsubscribe(Channel channel, MqttUnsubscribeMessage mqttMessage) {
        List<String> topics1 = mqttMessage.payload().topics();
        mqttChannelService.unsubscribe(mqttChannelService.getDeviceId(channel), topics1);
        unSubBack(channel, mqttMessage.variableHeader().messageId());
    }

    /**
     * 回复取消订阅
     */
    private void unSubBack(Channel channel, int messageId) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0x02);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        MqttUnsubAckMessage mqttUnsubAckMessage = new MqttUnsubAckMessage(mqttFixedHeader, variableHeader);
        channel.writeAndFlush(mqttUnsubAckMessage);
    }


    /**
     * 消息回复确认(qos1 级别 保证收到消息  但是可能会重复)
     */
    @Override
    public void puback(Channel channel, MqttMessage mqttMessage) {
        MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
        int messageId = messageIdVariableHeader.messageId();
        mqttChannelService.getMqttChannel(mqttChannelService.getDeviceId(channel)).getSendMqttMessage(messageId).setConfirmStatus(ConfirmStatus.COMPLETE); // 复制为空
    }


    /**
     * disconnect 主动断线
     */
    @Override
    public void disconnect(Channel channel) {
        mqttChannelService.closeSuccess(mqttChannelService.getDeviceId(channel), true);
    }


    /**
     * qos2 发布收到
     */
    @Override
    public void pubrec(Channel channel, MqttMessage mqttMessage ) {
        MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
        int messageId = messageIdVariableHeader.messageId();
        mqttChannelService.getMqttChannel(mqttChannelService.getDeviceId(channel)).getSendMqttMessage(messageId).setConfirmStatus(ConfirmStatus.PUBREL); // 复制为空
        mqttChannelService.doPubrec(channel, messageId);
    }

    /**
     * qos2 发布释放
     */
    @Override
    public void pubrel(Channel channel, MqttMessage mqttMessage ) {
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
        int messageId = mqttMessageIdVariableHeader.messageId();
        mqttChannelService.getMqttChannel(mqttChannelService.getDeviceId(channel)).getSendMqttMessage(messageId).setConfirmStatus(ConfirmStatus.COMPLETE); // 复制为空
        mqttChannelService.doPubrel(channel, messageId);

    }

    /**
     * qos2 发布完成
     */
    @Override
    public void pubcomp(Channel channel, MqttMessage mqttMessage ) {
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
        int messageId = mqttMessageIdVariableHeader.messageId();
        SendMqttMessage sendMqttMessage = mqttChannelService.getMqttChannel(mqttChannelService.getDeviceId(channel)).getSendMqttMessage(messageId);
        sendMqttMessage.setConfirmStatus(ConfirmStatus.COMPLETE); // 复制为空
    }

    @Override
    public void doTimeOut(Channel channel, IdleStateEvent evt) {
        logger.info("【PingPongService：doTimeOut 心跳超时】" + channel.remoteAddress() + "【channel 关闭】");
        switch (evt.state()) {
            case READER_IDLE:
                close(channel);
            case WRITER_IDLE:
                close(channel);
            case ALL_IDLE:
                close(channel);
        }
    }

}
