package com.lsx.component.mqttBroker.mqtt.bootstrap.bean;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * 保留消息
 **/
@Builder
@Data
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
