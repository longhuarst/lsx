package com.lsx.component.mqttbroker.mqtt.service;


import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;
import io.netty.handler.timeout.IdleStateEvent;

//抽象出服务端事件
public abstract class ServerMqttHandlerService implements MqttHandlerIntf {

    public abstract boolean login(Channel channel, MqttConnectMessage mqttConnectMessage);

    public abstract  void  publish(Channel channel, MqttPublishMessage mqttPublishMessage);

    public abstract void subscribe(Channel channel, MqttSubscribeMessage mqttSubscribeMessage);


    public abstract void pong(Channel channel);

    public abstract  void unsubscribe(Channel channel, MqttUnsubscribeMessage mqttMessage);


    public abstract void disconnect(Channel channel);

    public abstract void doTimeOut(Channel channel, IdleStateEvent evt);


}
