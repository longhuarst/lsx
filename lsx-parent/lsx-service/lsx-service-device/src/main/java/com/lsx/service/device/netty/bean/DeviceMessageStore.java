package com.lsx.service.device.netty.bean;

import java.io.Serializable;

public class DeviceMessageStore implements Serializable {


    String uuid;
    String topic;
    String msg;

    @Override
    public String toString() {
        return "DeviceMessageStore{" +
                "uuid='" + uuid + '\'' +
                ", topic='" + topic + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
