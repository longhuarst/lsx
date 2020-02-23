package com.lsx.component.mqttbroker.mqtt.bean;

import com.lsx.component.mqttbroker.mqtt.enums.ConfirmStatus;

public class SendMqttMessage {

    private String Topic;

    private byte[] payload;

    private int qos;

    private boolean retained;

    private boolean dup;

    private int messageId;


    private long timestamp;

    private volatile ConfirmStatus confirmStatus;


    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public boolean isRetained() {
        return retained;
    }

    public void setRetained(boolean retained) {
        this.retained = retained;
    }

    public boolean isDup() {
        return dup;
    }

    public void setDup(boolean dup) {
        this.dup = dup;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ConfirmStatus getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(ConfirmStatus confirmStatus) {
        this.confirmStatus = confirmStatus;
    }
}
