package com.lsx.component.mqttbroker.mqtt.bean;

import io.netty.handler.codec.mqtt.MqttQoS;

public class SessionMessage {

    private byte[]  byteBuf;

    private MqttQoS qoS;

    private  String topic;


    public String getString(){
        return new String(byteBuf);
    }


    public byte[] getByteBuf() {
        return byteBuf;
    }

    public void setByteBuf(byte[] byteBuf) {
        this.byteBuf = byteBuf;
    }

    public MqttQoS getQoS() {
        return qoS;
    }

    public void setQoS(MqttQoS qoS) {
        this.qoS = qoS;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
