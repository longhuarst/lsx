package com.lsx.component.mqttbroker.mqtt.bean;

import io.netty.handler.codec.mqtt.MqttQoS;

public class RetainMessage {

    private byte[]  byteBuf;

    private MqttQoS qoS;
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
}
